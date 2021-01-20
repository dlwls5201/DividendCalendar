package com.tistory.domain.repository

import com.tistory.domain.base.BaseListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModifyStockCountUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) {
    suspend fun build(ticker: String, stockCnt: Int, listener: BaseListener<Any>) {
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