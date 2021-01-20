package com.tistory.domain.usecase

import com.tistory.domain.base.FlowUseCase
import com.tistory.domain.model.StockWithDividendItem
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.flow.Flow

class GetStockItemsUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) : FlowUseCase<Any, Flow<List<StockWithDividendItem>>>() {

    override fun build(params: Any): Flow<List<StockWithDividendItem>> {
        return stockWithDividendRepository.getStockItems()
    }

    fun get(): Flow<List<StockWithDividendItem>> {
        return build(Any())
    }
}
