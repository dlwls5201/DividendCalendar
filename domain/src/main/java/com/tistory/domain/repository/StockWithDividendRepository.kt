package com.tistory.domain.repository

import com.tistory.domain.model.CalendarItem
import com.tistory.domain.model.StockWithDividendItem
import kotlinx.coroutines.flow.Flow

interface StockWithDividendRepository {

    fun getStockItems(): Flow<List<StockWithDividendItem>>

    fun getCalendarItems(): Flow<List<CalendarItem>>

    suspend fun modifyStockCnt(ticker: String, stockCnt: Int)

    suspend fun fetchAndPutStock(ticker: String, stockCnt: Int)

    suspend fun fetchAndPutDividends(ticker: String)

    suspend fun fetchAllStockDividend()

    suspend fun fetchAllStockNextDividend()

    suspend fun deleteStockWithDividends(ticker: String)

    suspend fun clearStock()
}