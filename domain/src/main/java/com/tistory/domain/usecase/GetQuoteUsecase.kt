package com.tistory.domain.usecase

import com.tistory.domain.base.BaseListener
import com.tistory.domain.base.SuspendUseCase
import com.tistory.domain.model.QuoteItem
import com.tistory.domain.repository.QuoteRepository

class GetQuoteUsecase(
    private val quoteRepository: QuoteRepository
) : SuspendUseCase<String, QuoteItem>() {

    override suspend fun onSuccess(params: String): QuoteItem {
        return quoteRepository.getQuote(params)
    }

    suspend fun get(ticker: String, listener: BaseListener<QuoteItem>) {
        build(ticker, listener)
    }
}
