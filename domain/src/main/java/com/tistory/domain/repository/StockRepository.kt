package com.tistory.domain.repository

import com.tistory.domain.model.Stock

interface StockRepository {

    suspend fun getStocks(): List<Stock>

    suspend fun getStock(ticker: String): Stock

    suspend fun putStock(ticker: String, stockCnt: Int)
}