package com.tistory.dividendcalendar.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tistory.domain.repository.StockWithDividendRepository

class CalendarViewModel(
    private val stockWithDividendRepository: StockWithDividendRepository
) : ViewModel() {

    val dividendItems =
        stockWithDividendRepository.getCalendarItems().asLiveData()
}