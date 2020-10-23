package com.tistory.domain.repository

import com.tistory.domain.model.CalendarItem
import kotlinx.coroutines.flow.Flow

interface StockWithDividendRepository {

    fun getCalendarItems(): Flow<List<CalendarItem>>

    suspend fun fetchAndPutStock(ticker: String, stockCnt: Int)

    suspend fun fetchAndPutDividends(ticker: String)

    suspend fun deleteStock(ticker: String)

    suspend fun clearStock()
}