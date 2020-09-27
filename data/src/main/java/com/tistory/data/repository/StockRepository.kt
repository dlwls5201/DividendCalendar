package com.tistory.data.repository

import com.tistory.data.base.BaseResponse
import com.tistory.data.source.local.entity.StockEntity

interface StockRepository {

    suspend fun getStock(ticker: String, listener: BaseResponse<StockEntity>)

    suspend fun putStock(ticker: String, stockCnt: Int, listener: BaseResponse<Any>)

    suspend fun deleteStockFromTicker(ticker: String, listener: BaseResponse<Any>)

    suspend fun loadNextDividendsFromTicker(ticker: String, listener: BaseResponse<Any>)

    suspend fun getAllDividendItems(listener: BaseResponse<List<DividendItem>>)
}