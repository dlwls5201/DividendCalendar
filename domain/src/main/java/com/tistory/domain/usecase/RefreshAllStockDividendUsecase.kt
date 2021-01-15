package com.tistory.domain.usecase

import com.tistory.domain.base.BaseListener
import com.tistory.domain.base.SuspendUseCase
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshAllStockDividendUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) : SuspendUseCase<Any, Any>() {
    suspend fun build(listener: BaseListener<Any>) {
        withContext(Dispatchers.Main) {
            try {
                listener.onLoading()
                withContext(Dispatchers.IO) {
                    stockWithDividendRepository.fetchAllStockNextDividend()
                }
                listener.onSuccess(Any())
            } catch (e: Exception) {
                listener.onError(e)
            }
            listener.onLoaded()
        }
    }

    override suspend fun onSuccess(params: Any) {
        stockWithDividendRepository.fetchAllStockNextDividend()
    }

    suspend fun get(listener: BaseListener<Any>) {
        build(Any(), listener)
    }
}
