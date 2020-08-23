package com.tistory.dividendcalendar.data.repository

import com.google.gson.Gson
import com.tistory.dividendcalendar.base.util.Dlog
import com.tistory.dividendcalendar.data.base.BaseResponse
import com.tistory.dividendcalendar.data.source.local.StockDao
import com.tistory.dividendcalendar.data.source.local.entity.DividendEntity
import com.tistory.dividendcalendar.data.source.local.entity.LogoEntity
import com.tistory.dividendcalendar.data.source.local.entity.SymbolEntity
import com.tistory.dividendcalendar.data.source.local.entity.mapToItem
import com.tistory.dividendcalendar.data.source.remote.ApiProvider
import com.tistory.dividendcalendar.data.source.remote.api.StockApi
import com.tistory.dividendcalendar.data.source.remote.model.DividendResponse
import com.tistory.dividendcalendar.data.source.remote.model.ProfileResponse
import com.tistory.dividendcalendar.presentation.model.DividendItem
import com.tistory.dividendcalendar.presentation.model.Range

class StockRepositoryImpl(
    private val stockDao: StockDao,
    private val stockApi: StockApi
) : StockRepository {

    private val token = ApiProvider.token

    private val gson = Gson()

    override suspend fun getLogo(symbol: String, listener: BaseResponse<String>) {
        listener.onLoading()

        try {
            val cacheLogo = stockDao.getLogo(symbol)
            if (cacheLogo == null) {
                val logoUrl = stockApi.getLogo(symbol, token).url
                stockDao.insertLogo(
                    LogoEntity(symbol, logoUrl)
                )
                listener.onSuccess(logoUrl)
            } else {
                listener.onSuccess(cacheLogo.logoUrl)
            }

        } catch (e: Exception) {
            listener.onError(e)
        }
        listener.onLoaded()
    }

    //TODO 필요한 파라미터 정보 확인
    override suspend fun getProfile(symbol: String, listener: BaseResponse<ProfileResponse>) {
        listener.onLoading()
        try {
            listener.onSuccess(stockApi.getProfile(symbol, token))
        } catch (e: Exception) {
            listener.onError(e)
        }
        listener.onLoaded()
    }

    /**
     * stockApi.getDividend
     *
     * 1. range가 next 일 때는 { } 형태로 데이터 반환
     * 2. 다음 배당일이 없는 경우 [ ] 빈 배열 형태로 데이터 반환
     *      - 2020.08.23 배당이 없는 회사와 배당이 있지만 다음 배당일이 선언 안된 회사를 구분할 수 없음
     * 3. 데이터는 있는데 amount가 없는 경우 -> 배당이 있지만 다음 배당일이 선언 안된 회사
     */
    override suspend fun getNextDividend(symbol: String, listener: BaseResponse<DividendItem>) {
        listener.onLoading()

        try {
            val cacheDividends = stockDao.getDividends(symbol)
            Dlog.d("cacheDividends : $cacheDividends")

            if (cacheDividends.isNullOrEmpty()) {
                Dlog.d("--- 서버로 부터 데이터 가져오기 ---")
                loadDividendAndCaching(symbol, listener::onSuccess, listener::onFail)
            } else {
                val cacheLastDividend = cacheDividends.last()
                val currentUnixTime = System.currentTimeMillis() / 1000

                val requestedData = cacheLastDividend.requestedDate

                //한달 전 데이터는 새로 업데이트 해줍니다.
                val diffDate = currentUnixTime - requestedData
                Dlog.d("diffDate : $diffDate -> check : ${checkMoreMonthTime(diffDate)}")

                if (checkMoreMonthTime(diffDate)) {
                    Dlog.d("--- 오래된 데이터 이므로 갱신 ---")
                    loadDividendAndCaching(symbol, listener::onSuccess, listener::onFail)
                } else {
                    Dlog.d("--- 캐싱된 데이터 가져오기 ---")
                    listener.onSuccess(cacheLastDividend.mapToItem())
                }
            }
        } catch (e: Exception) {
            listener.onError(e)
        }
        listener.onLoaded()
    }

    private suspend fun loadDividendAndCaching(
        symbol: String,
        onSuccess: (item: DividendItem) -> Unit,
        onFail: (description: String) -> Unit
    ) {
        val range = Range.NEXT

        val data = stockApi.getDividend(symbol = symbol, range = range.value, token = token)
        Dlog.d("data : $data")

        if (data.toString() == "[]") {
            onFail("다음 배당이 없습니다.")
        } else {
            val json = gson.toJson(data)
            val dividendResponse = gson.fromJson(json, DividendResponse::class.java)
            Dlog.d("dividendResponse : $dividendResponse")

            if (dividendResponse.amount.isNullOrEmpty() || dividendResponse.amount == "0.0") {
                onFail("다음 배당이 없습니다.")
                return
            }

            val dividendEntity = getAndInsertDividendEntity(symbol, dividendResponse)
            onSuccess(dividendEntity.mapToItem())
        }
    }

    private suspend fun getAndInsertDividendEntity(
        symbol: String,
        dividendResponse: DividendResponse
    ): DividendEntity {
        val cachedSymbol = stockDao.getSymbol(symbol)
        if (cachedSymbol == null) {
            stockDao.insertSymbol(SymbolEntity(symbol))
        }

        val currentUnixTime = System.currentTimeMillis() / 1000

        val dividendEntity = DividendEntity(
            declaredDate = dividendResponse.declaredDate,
            parentSymbol = symbol,

            exDate = dividendResponse.exDate,
            paymentDate = dividendResponse.paymentDate,
            recordDate = dividendResponse.recordDate,
            amount = dividendResponse.amount.toFloatOrNull() ?: 0f,
            frequency = dividendResponse.frequency,
            requestedDate = currentUnixTime
        )

        stockDao.insertDividend(dividendEntity)

        return dividendEntity
    }

    private fun checkMoreMonthTime(diffDate: Long): Boolean {
        val monthSecond = 60 * 60 * 24 * 30
        //TODO [test] return diffDate >= monthSecond
        return diffDate >= 300 // 5분에 한번씩 데이터를 갱신  한다.
    }
}