package com.tistory.data.repository

import com.tistory.data.source.local.entity.StockNameEntity

interface StockNameRepository {

    suspend fun getStocksFromQuery(query: String): List<StockNameEntity>

    suspend fun insertStock(ticker: String, companyName: String)

    suspend fun clearStocks()
}