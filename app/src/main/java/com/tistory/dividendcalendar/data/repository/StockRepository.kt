package com.tistory.dividendcalendar.data.repository

import com.tistory.dividendcalendar.data.base.BaseResponse
import com.tistory.dividendcalendar.data.source.local.entity.StockEntity
import com.tistory.dividendcalendar.presentation.model.DividendItem

interface StockRepository {

    suspend fun getStock(ticker: String, listener: BaseResponse<StockEntity>)

    suspend fun putStock(ticker: String, stockCnt: Int, listener: BaseResponse<Any>)

    suspend fun loadNextDividendsFromTicker(ticker: String)

    suspend fun getAllDividendItems(listener: BaseResponse<List<DividendItem>>)
}