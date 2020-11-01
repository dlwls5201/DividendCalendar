package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.databinding.FragmentCalendarBinding
import com.tistory.dividendcalendar.di.Injection
import com.tistory.dividendcalendar.presentation.calendar.view.DividendCalendarView
import com.tistory.dividendcalendar.presentation.dialog.DividendsDialogFragment
import com.tistory.domain.model.CalendarItem

class CalendarFragment : DividendFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    override var logTag = "CalendarFragment"

    companion object {

        fun newInstance() = CalendarFragment()
    }

    private val calendarViewModel by lazy {
        ViewModelProvider(
            this, CalendarViewModelFactory(
                Injection.provideStockWithDividendRepo()
            )
        ).get(CalendarViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarViewModel = calendarViewModel
        binding.dividendCalendarView.setEventHandler(object : DividendCalendarView.EventHandler {
            override fun onDayLongPress(items: List<CalendarItem>) {
                Dlog.d("onDayLongPress items : $items")
                DividendsDialogFragment.newInstance(items)
                    .show(childFragmentManager, DividendsDialogFragment.TAG)
            }

            override fun onDayPress(items: List<CalendarItem>) {
                Dlog.d("onDayPress items : $items")
                if (items.isNotEmpty()) {
                    DividendsDialogFragment.newInstance(items)
                        .show(childFragmentManager, DividendsDialogFragment.TAG)
                }
            }
        })
    }

    override fun onViewModelSetup() {
        calendarViewModel.dividendItems.observe(viewLifecycleOwner, Observer {
            binding.dividendCalendarView.updateCalendar(it)
        })
    }
}