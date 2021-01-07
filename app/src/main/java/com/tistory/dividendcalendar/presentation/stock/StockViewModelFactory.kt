package com.tistory.dividendcalendar.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tistory.domain.repository.StockWithDividendRepository

class StockViewModelFactory(
    private val stockWithDividendRepository: StockWithDividendRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            StockViewModel(stockWithDividendRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}