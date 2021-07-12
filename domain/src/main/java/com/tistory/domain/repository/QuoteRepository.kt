package com.tistory.domain.repository

import com.tistory.domain.model.QuoteItem


interface QuoteRepository {

    suspend fun getQuote(ticker: String): QuoteItem
}
