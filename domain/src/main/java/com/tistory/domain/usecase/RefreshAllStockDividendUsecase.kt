package com.tistory.domain.usecase

import com.tistory.domain.base.BaseListener
import com.tistory.domain.base.SuspendUseCase
import com.tistory.domain.repository.StockWithDividendRepository

class RefreshAllStockDividendUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) : SuspendUseCase<Any, Any>() {

    override suspend fun onSuccess(params: Any) {
        stockWithDividendRepository.fetchAllStockNextDividend()
    }

    suspend fun get(listener: BaseListener<Any>) {
        build(Any(), listener)
    }
}
