package com.tistory.domain.usecase

import com.tistory.domain.repository.StockRepository

class StockUsecase(
    private val stockRepository: StockRepository
)