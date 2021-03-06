package com.tistory.data.repository

import com.google.gson.Gson
import com.tistory.blackjinbase.util.Dlog
import com.tistory.data.ext.toFloatCheckFormat
import com.tistory.data.source.local.StockDao
import com.tistory.data.source.local.entity.DividendEntity
import com.tistory.data.source.local.entity.StockEntity
import com.tistory.data.source.local.entity.mapToItem
import com.tistory.data.source.local.entity.mapToStockItem
import com.tistory.data.source.remote.api.StockApi
import com.tistory.data.source.remote.model.DividendResponse
import com.tistory.domain.model.CalendarItem
import com.tistory.domain.model.StockWithDividendItem
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray

class StockWithDividendRepositoryImpl(
    private val stockDao: StockDao,
    private val stockApi: StockApi
) : StockWithDividendRepository {

    companion object {
        private const val ONE_YEAR = "1y"
    }

    private val gson = Gson()

    override fun getStockItems(): Flow<List<StockWithDividendItem>> {
        return stockDao.getStockWithDividends().map {
            it.map { entity -> entity.mapToStockItem() }
        }
    }

    override fun getCalendarItems(): Flow<List<CalendarItem>> {
        return stockDao.getStockWithDividends().flatMapConcat {
            flow {
                val tempCalendarItems = mutableListOf<CalendarItem>()
                it.forEach { entity ->
                    entity.dividends.forEach {
                        tempCalendarItems.add(
                            it.mapToItem(entity.stock)
                        )
                    }
                }
                emit(tempCalendarItems)
            }
        }
    }

    override suspend fun modifyStockCnt(ticker: String, stockCnt: Float) {
        val symbol = ticker.toUpperCase()
        val stockEntity = stockDao.getStock(symbol)
        Dlog.d("stockEntity : $stockEntity")

        if (stockEntity == null) {
            fetchAndPutStock(ticker, stockCnt)
        } else {
            val stock = stockEntity.copy(stockCnt = stockCnt)
            Dlog.d("modify stock : $stock")
            stockDao.updateStock(stock)
        }
    }

    override suspend fun fetchAndPutStock(ticker: String, stockCnt: Float) {
        val symbol = ticker.toUpperCase()
        val stockEntity = stockDao.getStock(symbol)
        Dlog.d("stockEntity : $stockEntity")

        if (stockEntity == null) {
            coroutineScope {
                val logoResponse = async { stockApi.getLogo(symbol) }
                val profileResponse = async { stockApi.getProfile(symbol) }

                val logo = logoResponse.await()
                val profile = profileResponse.await()

                val stock = StockEntity(
                    symbol = symbol,
                    stockCnt = stockCnt,
                    logoUrl = logo.url,
                    companyName = profile.companyName ?: ""
                )

                Dlog.d("insert stock : $stock")
                stockDao.insertStock(stock)
            }
        } else {
            val copyStock = stockEntity.copy(stockCnt = stockCnt)
            Dlog.d("update copyStock : $copyStock")
            stockDao.updateStock(copyStock)
        }
    }

    override suspend fun fetchAndPutDividends(ticker: String) {
        fetchAndPutNextDividends(ticker)
        val symbol = ticker.toUpperCase()
        val dividends =
            stockApi.getDividends(symbol, ONE_YEAR).filter { it.amount.isNotEmpty() }
        Dlog.d("dividends : $dividends")

        dividends.forEach {
            if (isValidStockDividendValue(it.amount, it.declaredDate)) {
                stockDao.insertDividend(
                    DividendEntity(
                        dividendId = DividendEntity.makeDividendId(symbol, it.paymentDate),
                        parentSymbol = symbol,
                        exDate = it.exDate,
                        declaredDate = it.declaredDate,
                        paymentDate = it.paymentDate,
                        recordDate = it.date,
                        amount = it.amount.toFloatCheckFormat(),
                        frequency = it.frequency
                    )
                )
            }
        }
    }

    override suspend fun fetchAllStockNextDividend() {
        stockDao.getStockList().forEach { stock ->
            Dlog.d("stock : ${stock.symbol}")

            withContext(Dispatchers.Default) {
                Dlog.d("stock : ${stock.symbol} -> fetchAllStockDividend")
                fetchAndPutNextDividends(stock.symbol)
            }
        }
    }

    private suspend fun fetchAndPutNextDividends(ticker: String) {
        val symbol = ticker.toUpperCase()
        val stockWithDividend = stockDao.getStockWithDividend(symbol)
        Dlog.d("$symbol -> stockWithDividend : $stockWithDividend")

        if (stockWithDividend != null) {

            val nextDividend = stockApi.getDividend(symbol)
            Dlog.d("nextDividend : $nextDividend")

            val jsonString = gson.toJson(nextDividend)
            try {
                val jsonArray = JSONArray(jsonString)
                val size = jsonArray.length()
                Dlog.d("jsonArray length : $size")

                val hasNextDividend = size != 0
                Dlog.d("hasNextDividend $hasNextDividend")

                stockDao.updateStock(
                    stockWithDividend.stock
                )

                if (hasNextDividend) {
                    val jsonObject = jsonArray.get(0)
                    Dlog.d("jsonObject : $jsonObject")

                    val tempDividend =
                        gson.fromJson(jsonObject.toString(), DividendResponse::class.java)
                    Dlog.d("tempDividend : $tempDividend")

                    tempDividend.let {
                        if (isValidStockDividendValue(it.amount, it.declaredDate)) {
                            stockDao.insertDividend(
                                DividendEntity(
                                    dividendId = DividendEntity.makeDividendId(symbol, it.paymentDate),
                                    parentSymbol = symbol,
                                    exDate = it.exDate,
                                    declaredDate = it.declaredDate,
                                    paymentDate = it.paymentDate,
                                    recordDate = it.date,
                                    amount = it.amount.toFloatCheckFormat(),
                                    frequency = it.frequency
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Dlog.e(e.message)
            }

        } else {
            Dlog.d("다음 배당금이 없습니다")
        }
    }

    private fun isValidStockDividendValue(amount: String, declaredDate: String) = amount != "0" && declaredDate != "0000-00-00"

    override suspend fun deleteStockWithDividends(ticker: String) {
        val symbol = ticker.toUpperCase()
        Dlog.d("$symbol -> deleteStockWithDividends")
        stockDao.deleteStockWithDividends(symbol)
    }
}