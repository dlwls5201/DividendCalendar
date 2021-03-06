package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.databinding.FragmentCalendarBinding
import com.tistory.dividendcalendar.presentation.calendar.view.DividendCalendarView
import com.tistory.dividendcalendar.presentation.dialog.DividendsDialogFragment
import com.tistory.domain.model.CalendarItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : DividendFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    companion object {

        fun newInstance() = CalendarFragment()
    }

    override var logTag = "CalendarFragment"

    private val calendarViewModel by viewModels<CalendarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarViewModel = calendarViewModel
        initCalendarView()
        initButton()

    }

    private fun initCalendarView() {
        binding.dividendCalendarView.setEventHandler(object : DividendCalendarView.EventHandler {
            override fun onDayLongPress(items: List<CalendarItem>) {
                Dlog.d("onDayLongPress items : $items")
                if (items.isNotEmpty()) {
                    DividendsDialogFragment.newInstance(items)
                        .show(childFragmentManager, DividendsDialogFragment.TAG)
                }
            }

            override fun onDayPress(items: List<CalendarItem>) {
                Dlog.d("onDayPress items : $items")
                if (items.isNotEmpty()) {
                    DividendsDialogFragment.newInstance(items)
                        .show(childFragmentManager, DividendsDialogFragment.TAG)
                }
            }
        })

        binding.dividendCalendarView.setTotalDividendListener(::setCalculateTotalDividend)
    }

    private fun setCalculateTotalDividend(title: String) {
        binding.tvCalendarTotalDividend.text = title
    }

    private fun initButton() {
        binding.cvCalendarTotalDividend.setOnClickListener {
            binding.dividendCalendarView.changeCalendarType()
        }
    }

    override fun onViewModelSetup() {
        calendarViewModel.dividendItems.observe(viewLifecycleOwner) {
            binding.dividendCalendarView.updateCalendar(it)
        }
    }
}