package com.tistory.data.repository

import com.tistory.data.source.local.StockNameDao
import com.tistory.data.source.local.entity.StockNameEntity

class StockNameRepositoryImpl(
    private val tickerDao: StockNameDao
) : StockNameRepository {

    override suspend fun getStocksFromQuery(query: String) = tickerDao.getTickers("$query%")

    override suspend fun insertStock(ticker: String, companyName: String) {
        val entity = StockNameEntity(
            ticker = ticker,
            companyName = companyName
        )
        tickerDao.insertTicker(entity)
    }

    override suspend fun clearStocks() {
        tickerDao.clearTickers()
    }
}