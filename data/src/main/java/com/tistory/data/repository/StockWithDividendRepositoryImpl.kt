package com.tistory.data.repository

import com.google.gson.Gson
import com.tistory.blackjinbase.ext.toFloatCheckFormat
import com.tistory.blackjinbase.util.Dlog
import com.tistory.data.source.local.StockDao
import com.tistory.data.source.local.entity.DividendEntity
import com.tistory.data.source.local.entity.StockEntity
import com.tistory.data.source.local.entity.mapToItem
import com.tistory.data.source.local.entity.mapToStockItem
import com.tistory.data.source.remote.api.StockApi
import com.tistory.data.source.remote.model.DividendResponse
import com.tistory.domain.model.CalendarItem
import com.tistory.domain.model.StockItem
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StockWithDividendRepositoryImpl(
    private val stockDao: StockDao,
    private val stockApi: StockApi
) : StockWithDividendRepository {

    private val gson = Gson()

    /*fun getStocks(): Flow<List<StockEntity>> {
        return stockDao.getStocks()
    }

    fun getDividends(): Flow<List<DividendEntity>> {
        return stockDao.getDividends()
    }


    suspend fun getStock(ticker: String): StockEntity? {
        val symbol = ticker.toUpperCase()
        val stockEntity = stockDao.getStock(symbol)
        Dlog.d("stockEntity : $stockEntity")
        return stockEntity
    }

    suspend fun getStockWithDividend(ticker: String): StockWithDividendEntity? {
        val symbol = ticker.toUpperCase()
        val stockWithDividendEntity = stockDao.getStockWithDividend(symbol)
        Dlog.d("stockWithDividendEntity : $stockWithDividendEntity")
        return stockWithDividendEntity
    }*/

    override fun getStockItems(): Flow<List<StockItem>> {
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

    override suspend fun fetchAndPutStock(ticker: String, stockCnt: Int) {
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
            Dlog.d("insert copyStock : $copyStock")
            stockDao.insertStock(copyStock)
        }
    }

    override suspend fun fetchAndPutDividends(ticker: String) {
        val symbol = ticker.toUpperCase()
        val stockWithDividend = stockDao.getStockWithDividend(symbol)
        Dlog.d("$symbol -> stockWithDividend : $stockWithDividend")

        if (stockWithDividend != null) {

            val nextDividend = stockApi.getDividend(symbol)
            Dlog.d("nextDividend : $nextDividend")

            stockDao.deleteDividends(symbol)
            Dlog.d("deleteDividends")

            val hasNextDividend = nextDividend.toString() != "[]"
            stockDao.insertStock(
                stockWithDividend.stock.copy(hasNextDividend = hasNextDividend)
            )

            if (hasNextDividend) {
                val jsonString = gson.toJson(nextDividend)
                Dlog.d("jsonString : $jsonString")

                val tempDividend =
                    gson.fromJson(jsonString, DividendResponse::class.java)
                Dlog.d("tempDividend : $tempDividend")

                tempDividend.let {
                    stockDao.insertDividend(
                        DividendEntity(
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
            } else {
                Dlog.d("다음 배당금이 없습니다")
            }

            val dividends = stockApi.getDividends(symbol, "1y")
            Dlog.d("dividends : $dividends")

            dividends.forEach {
                stockDao.insertDividend(
                    DividendEntity(
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

    override suspend fun deleteStock(ticker: String) {
        val symbol = ticker.toUpperCase()
        Dlog.d("$symbol -> deleteStock")
        stockDao.deleteStock(symbol)
    }

    override suspend fun clearStock() {
        Dlog.d("clearStock")
        stockDao.clearStocks()
    }
}