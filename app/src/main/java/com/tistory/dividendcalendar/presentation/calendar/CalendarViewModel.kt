package com.tistory.dividendcalendar.presentation.calendar

import com.tistory.dividendcalendar.base.BaseViewModel
import com.tistory.dividendcalendar.data.repository.StockRepository

class CalendarViewModel(
    private val stockRepository: StockRepository
) : BaseViewModel() {
}