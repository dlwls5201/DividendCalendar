package com.tistory.domain.usecase

import com.tistory.domain.base.BaseListener
import com.tistory.domain.base.SuspendUseCase
import com.tistory.domain.repository.StockWithDividendRepository

class AddStockUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) : SuspendUseCase<Pair<String, Float>, Any>() {

    override suspend fun onSuccess(params: Pair<String, Float>) {
        val (ticker, stockCnt) = params
        stockWithDividendRepository.fetchAndPutStock(ticker, stockCnt)
        stockWithDividendRepository.fetchAndPutDividends(ticker)
    }

    suspend fun get(ticker: String, stockCnt: Float, listener: BaseListener<Any>) {
        build(Pair(ticker, stockCnt), listener)
    }
}
