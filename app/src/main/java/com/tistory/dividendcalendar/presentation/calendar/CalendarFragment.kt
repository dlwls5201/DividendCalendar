package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseFragment
import com.tistory.dividendcalendar.data.injection.Injection
import com.tistory.dividendcalendar.databinding.FragmentCalendarBinding
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    companion object {

        fun newInstance() = CalendarFragment()
    }

    private val calendarViewModel by lazy {
        ViewModelProvider(
            this, CalendarViewModelFactory(
                Injection.provideStockRepository()
            )
        ).get(CalendarViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarViewModel = calendarViewModel

        calendarViewModel.loadDividendItems()

        calendarViewModel.dividendItems.observe(viewLifecycleOwner, Observer {
            dividendCalendarView.updateCalendar(it)
        })
    }
}