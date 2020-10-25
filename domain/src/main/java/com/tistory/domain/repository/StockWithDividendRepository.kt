package com.tistory.domain.repository

import com.tistory.domain.model.CalendarItem
import com.tistory.domain.model.StockItem
import kotlinx.coroutines.flow.Flow

interface StockWithDividendRepository {

    fun getStockItems(): Flow<List<StockItem>>

    fun getCalendarItems(): Flow<List<CalendarItem>>

    suspend fun fetchAndPutStock(ticker: String, stockCnt: Int)

    suspend fun fetchAndPutDividends(ticker: String)

    suspend fun deleteStock(ticker: String)

    suspend fun clearStock()
}