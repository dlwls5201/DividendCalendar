package com.tistory.domain.usecase

import com.tistory.domain.base.BaseListener
import com.tistory.domain.base.SuspendUseCase
import com.tistory.domain.repository.StockWithDividendRepository

class DeleteStockUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) : SuspendUseCase<String, Any>() {

    override suspend fun onSuccess(params: String) {
        stockWithDividendRepository.deleteStockWithDividends(params)
    }

    suspend fun get(ticker: String, listener: BaseListener<Any>) {
        build(ticker, listener)
    }
}
