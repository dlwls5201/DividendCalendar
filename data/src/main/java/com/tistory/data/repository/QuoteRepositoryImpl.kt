package com.tistory.data.repository

import com.tistory.data.source.local.StockDao
import com.tistory.data.source.remote.api.StockApi
import com.tistory.domain.model.QuoteItem
import com.tistory.domain.repository.QuoteRepository

class QuoteRepositoryImpl(
    private val stockDao: StockDao,
    private val stockApi: StockApi
) : QuoteRepository {

    override suspend fun getQuote(ticker: String): QuoteItem {
        val symbol = ticker.toUpperCase()
        val result = stockApi.getQuote(symbol)
        val stock = stockDao.getStock(symbol)

        return QuoteItem(
            ticker = symbol,
            companyName = result.companyName ?: "",
            logoUrl = stock?.logoUrl ?: "",
            latestPrice = result.latestPrice ?: 0.0,
            week52High = result.week52High ?: 0.0,
            week52Low = result.week52Low ?: 0.0
        )
    }
}
