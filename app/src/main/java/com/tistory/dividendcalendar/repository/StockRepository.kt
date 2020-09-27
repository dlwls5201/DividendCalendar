package com.tistory.dividendcalendar.repository

import com.tistory.data.source.local.entity.StockEntity
import com.tistory.dividendcalendar.presentation.model.DividendItem
import com.tistory.dividendcalendar.repository.base.BaseResponse

interface StockRepository {

    suspend fun getStock(ticker: String, listener: BaseResponse<StockEntity>)

    suspend fun putStock(ticker: String, stockCnt: Int, listener: BaseResponse<Any>)

    suspend fun deleteStockFromTicker(ticker: String, listener: BaseResponse<Any>)

    suspend fun loadNextDividendsFromTicker(ticker: String, listener: BaseResponse<Any>)

    suspend fun getAllDividendItems(listener: BaseResponse<List<DividendItem>>)
}