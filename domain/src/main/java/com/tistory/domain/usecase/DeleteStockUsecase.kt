package com.tistory.domain.usecase

import com.tistory.domain.base.BaseListener
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteStockUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) {
    suspend fun build(ticker: String, listener: BaseListener<Any>) {
        withContext(Dispatchers.Main) {
            try {
                listener.onLoading()
                withContext(Dispatchers.IO) {
                    stockWithDividendRepository.deleteStockWithDividends(ticker)
                }
                listener.onSuccess(Any())
            } catch (e: Exception) {
                listener.onError(e)
            }
            listener.onLoaded()
        }
    }

}