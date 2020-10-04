package com.tistory.dividendcalendar.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tistory.domain.usecase.StockUsecase

class CalendarViewModelFactory(
    private val stockUsecase: StockUsecase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            CalendarViewModel(stockUsecase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}