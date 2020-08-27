package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseFragment
import com.tistory.dividendcalendar.base.util.Dlog
import com.tistory.dividendcalendar.data.injection.Injection
import com.tistory.dividendcalendar.databinding.FragmentCalendarBinding
import com.tistory.dividendcalendar.presentation.calendar.dialog.DividendsDialogFragment
import com.tistory.dividendcalendar.presentation.calendar.view.DividendCalendarView
import com.tistory.dividendcalendar.presentation.model.DividendItem
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

        dividendCalendarView.setEventHandler(object : DividendCalendarView.EventHandler {
            override fun onDayLongPress(items: List<DividendItem>) {
                Dlog.d("onDayLongPress items : $items")
                DividendsDialogFragment.newInstance(items)
                    .show(childFragmentManager, DividendsDialogFragment.TAG)
            }

            override fun onDayPress(items: List<DividendItem>) {
                Dlog.d("onDayPress items : $items")
                if (items.isNotEmpty()) {
                    DividendsDialogFragment.newInstance(items)
                        .show(childFragmentManager, DividendsDialogFragment.TAG)
                }
            }
        })

        calendarViewModel.loadDividendItems()

        calendarViewModel.dividendItems.observe(viewLifecycleOwner, Observer {
            dividendCalendarView.updateCalendar(it)
        })
    }
}