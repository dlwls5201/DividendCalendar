package com.tistory.data.repository

import com.google.gson.Gson
import com.tistory.blackjinbase.util.Dlog
import com.tistory.data.base.BaseResponse
import com.tistory.data.source.local.StockDao
import com.tistory.data.source.local.entity.DividendEntity
import com.tistory.data.source.local.entity.StockEntity
import com.tistory.data.source.local.entity.mapToItem
import com.tistory.data.source.remote.ApiProvider
import com.tistory.data.source.remote.api.StockApi
import com.tistory.data.source.remote.model.DividendResponse

class StockRepositoryImpl(
    private val stockDao: StockDao,
    private val stockApi: StockApi
) : StockRepository {

    private val token = ApiProvider.token

    private val gson = Gson()

    override suspend fun getStock(ticker: String, listener: BaseResponse<StockEntity>) {
        listener.onLoading()

        val symbol = ticker.toUpperCase()

        try {
            val cacheStock = stockDao.getStock(symbol)
            Dlog.d("cacheStock : $cacheStock")

            if (cacheStock == null) {
                val logo = stockApi.getLogo(symbol, token)
                val profile = stockApi.getProfile(symbol, token)
                Dlog.d("logo : $logo , profile : $profile")
                val stock = StockEntity.create(symbol, 0, logo, profile)
                stockDao.insertStock(stock)

                listener.onSuccess(stock)
            } else {
                listener.onSuccess(cacheStock)
            }
        } catch (e: Exception) {
            listener.onError(e)
        }
        listener.onLoaded()
    }

    override suspend fun putStock(
        ticker: String,
        stockCnt: Int,
        listener: BaseResponse<Any>
    ) {
        listener.onLoading()

        val symbol = ticker.toUpperCase()

        try {
            val cacheStock = stockDao.getStock(symbol)
            Dlog.d("cacheStock : $cacheStock")

            if (cacheStock == null) {
                val logo = stockApi.getLogo(symbol, token)
                val profile = stockApi.getProfile(symbol, token)
                Dlog.d("logo : $logo , profile : $profile")
                stockDao.insertStock(StockEntity.create(symbol, stockCnt, logo, profile))
            } else {
                if (stockCnt != cacheStock.stockCnt) {
                    Dlog.d("update stock : $stockCnt")
                    stockDao.insertStock(cacheStock.copy(stockCnt = stockCnt))
                }
            }

            listener.onSuccess(Any())
        } catch (e: Exception) {
            listener.onError(e)
        }
        listener.onLoaded()
    }

    override suspend fun deleteStockFromTicker(ticker: String, listener: BaseResponse<Any>) {
        listener.onLoading()

        try {
            val symbol = ticker.toUpperCase()
            stockDao.deleteProfileBySymbol(symbol)

            listener.onSuccess(ticker)
        } catch (e: Exception) {
            listener.onError(e)
        }

        listener.onLoaded()
    }

    override suspend fun loadNextDividendsFromTicker(ticker: String, listener: BaseResponse<Any>) {
        listener.onLoading()

        try {
            val symbol = ticker.toUpperCase()

            Dlog.d("--- $symbol 서버로 부터 데이터 가져오기 ---")
            loadDividendAndCaching(symbol)
            listener.onSuccess(Any())
        } catch (e: Exception) {
            listener.onError(e)
        }

        listener.onLoaded()
    }

    override suspend fun getAllDividendItems(listener: BaseResponse<List<DividendItem>>) {
        listener.onLoading()

        try {
            val cacheStocks = stockDao.getSortingStockWithDividends()
                .filter { it.stock.stockCnt != 0 }
            Dlog.d("cacheStocks : $cacheStocks")

            //최신 배당금 정보로 갱신 해줍니다.
            cacheStocks.forEach { stock ->
                loadNextDividendsFromTicker(stock.stock.symbol)
            }

            val dividendItems = mutableListOf<DividendItem>()

            //TODO 비효율적인 구조로 개선이 필요합니다.
            val updatedDividedStocks = stockDao.getSortingStockWithDividends()
                .filter { it.stock.stockCnt != 0 }
            Dlog.d("updatedDividedStocks size : ${updatedDividedStocks.size}")

            updatedDividedStocks.forEach { stock ->
                val lastDividend = stock.dividends.last()
                Dlog.d("getAllDividendItems lastDividend : $lastDividend")
                dividendItems.add(lastDividend.mapToItem(stock.stock))
            }

            Dlog.d("getAllDividendItems onSuccess size : ${dividendItems.size}")
            listener.onSuccess(dividendItems)
        } catch (e: Exception) {
            listener.onError(e)
        }
        listener.onLoaded()
    }

    /**
     * stockApi.getDividend
     *
     * 1. range 가 next 일 때는 { } 형태로 데이터 반환
     * 2. 다음 배당일이 없는 경우 [ ] 빈 배열 형태로 데이터 반환
     *      - 2020.08.23 배당이 없는 회사와 다음 배당일이 발표 안된 회사를 구분할 수 없음
     * 3. 데이터는 있는데 amount가 없는 경우 -> 배당이 있지만 다음 배당일이 선언 안된 회사
     */
    private suspend fun loadNextDividendsFromTicker(
        ticker: String
    ) {

        val symbol = ticker.toUpperCase()

        try {
            val cacheStockWithDividends = stockDao.getStockWithDividend(symbol)
            Dlog.d("$symbol : cacheStockWithDividends : $cacheStockWithDividends")

            if (cacheStockWithDividends == null || cacheStockWithDividends.dividends.isNullOrEmpty()) {
                Dlog.d("--- $symbol 서버로 부터 데이터 가져오기 ---")
                loadDividendAndCaching(symbol)
            } else {
                val cacheLastDividend = cacheStockWithDividends.dividends.last()
                val currentUnixTime = System.currentTimeMillis() / 1000

                val requestedData = cacheLastDividend.requestedDate

                //한달 전 데이터는 새로 업데이트 해줍니다.
                val diffDate = currentUnixTime - requestedData
                Dlog.d("diffDate : $diffDate -> check : ${checkOverMonthTime(diffDate)}")

                if (checkOverMonthTime(diffDate)) {
                    Dlog.d("--- $symbol 오래된 데이터 이므로 갱신 ---")
                    loadDividendAndCaching(symbol)
                } else {
                    Dlog.d("--- $symbol 캐싱된 데이터 시용 ---")
                }
            }
        } catch (e: Exception) {
            Dlog.e(e.message)
        }
    }

    private suspend fun loadDividendAndCaching(symbol: String) {
        val range = Range.NEXT

        val data = stockApi.getDividend(symbol = symbol, range = range.value, token = token)
        Dlog.d("data : $data")

        if (data.toString() == "[]") {
            Dlog.d("$symbol 다음 배당이 없습니다.")
            val dummyDividend = DividendEntity.createDummy(symbol)
            Dlog.d("dummy data insert : $dummyDividend")
            stockDao.insertDividend(dummyDividend)
        } else {
            val json = gson.toJson(data)
            val dividendResponse = gson.fromJson(json, DividendResponse::class.java)
            Dlog.d("dividendResponse : $dividendResponse")

            if (dividendResponse.amount.isEmpty() || dividendResponse.amount == "0.0") {
                Dlog.d("$symbol 다음 배당이 없습니다.")

                val dividend = DividendEntity.createDummy(symbol)
                Dlog.d("insert : $dividend")
                stockDao.insertDividend(dividend)
                return
            }

            val dividend = DividendEntity.create(symbol, dividendResponse)
            Dlog.d("insert : $dividend")
            stockDao.insertDividend(dividend)
        }
    }

    private fun checkOverMonthTime(diffDate: Long): Boolean {
        val monthSecond = 60 * 60 * 24 * 30
        return diffDate >= monthSecond
    }
}