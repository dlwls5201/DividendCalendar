package com.tistory.domain.usecase

import com.tistory.domain.repository.StockWithDividendRepository

class GetStockItemsUsecase(
    private val stockWithDividendRepository: StockWithDividendRepository
) {

    fun build() = stockWithDividendRepository.getStockItems()
}