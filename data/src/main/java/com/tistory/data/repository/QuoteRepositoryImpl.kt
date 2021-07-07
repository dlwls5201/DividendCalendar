package com.tistory.data.repository

import com.tistory.data.source.remote.api.StockApi
import com.tistory.domain.model.QuoteItem
import com.tistory.domain.repository.QuoteRepository

class QuoteRepositoryImpl(
    private val stockApi: StockApi
) : QuoteRepository {

    override suspend fun getQuote(ticker: String): QuoteItem {
        val symbol = ticker.toUpperCase()
        val result = stockApi.getQuote(symbol)
        return QuoteItem(
            symbol = symbol,
            companyName = result.companyName ?: "",
            latestPrice = result.latestPrice ?: 0.0,
            week52High = result.week52High ?: 0.0,
            week52Low = result.week52Low ?: 0.0
        )
    }
}
