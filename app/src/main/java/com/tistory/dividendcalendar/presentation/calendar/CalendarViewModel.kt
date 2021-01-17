package com.tistory.dividendcalendar.presentation.calendar

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tistory.domain.repository.StockWithDividendRepository

class CalendarViewModel @ViewModelInject constructor(
    stockWithDividendRepository: StockWithDividendRepository
) : ViewModel() {

    val dividendItems =
        stockWithDividendRepository.getCalendarItems().asLiveData()
}