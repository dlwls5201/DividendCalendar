package com.tistory.domain.usecase

import com.tistory.domain.base.BaseListener
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModifyStockCountUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) {
    suspend fun build(ticker: String, stockCnt: Float, listener: BaseListener<Any>) {
        withContext(Dispatchers.Main) {
            try {
                listener.onLoading()
                withContext(Dispatchers.IO) {
                    stockWithDividendRepository.modifyStockCnt(ticker, stockCnt)
                }
                listener.onSuccess(Any())
            } catch (e: Exception) {
                listener.onError(e)
            }
            listener.onLoaded()
        }
    }

}