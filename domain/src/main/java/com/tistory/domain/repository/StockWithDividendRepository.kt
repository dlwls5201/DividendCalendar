package com.tistory.domain.repository

import com.tistory.domain.model.CalendarItem
import com.tistory.domain.model.StockWithDividendItem
import kotlinx.coroutines.flow.Flow

interface StockWithDividendRepository {

    fun getStockItems(): Flow<List<StockWithDividendItem>>

    fun getCalendarItems(): Flow<List<CalendarItem>>

    suspend fun modifyStockCnt(ticker: String, stockCnt: Float)

    suspend fun fetchAndPutStock(ticker: String, stockCnt: Float)

    suspend fun fetchAndPutDividends(ticker: String)

    suspend fun fetchAllStockNextDividend()

    suspend fun deleteStockWithDividends(ticker: String)
}