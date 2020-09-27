package com.tistory.dividendcalendar.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tistory.data.repository.StockRepository

class CalendarViewModelFactory(
    private val stockRepository: StockRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            CalendarViewModel(stockRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}