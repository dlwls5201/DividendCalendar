package com.tistory.dividendcalendar.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tistory.domain.repository.StockWithDividendRepository

class CalendarViewModelFactory(
    private val stockWithDividendRepository: StockWithDividendRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            CalendarViewModel(stockWithDividendRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}