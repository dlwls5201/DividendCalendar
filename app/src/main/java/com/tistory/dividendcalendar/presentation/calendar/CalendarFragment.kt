package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseFragment
import com.tistory.dividendcalendar.data.injection.Injection
import com.tistory.dividendcalendar.databinding.FragmentCalendarBinding

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    companion object {

        fun newInstance() = CalendarFragment()
    }

    private val calendarViewModel by lazy {
        ViewModelProvider(
            this, CalendarViewModelFactory(
                Injection.provideInvitationRepository()
            )
        ).get(CalendarViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarViewModel = calendarViewModel
    }
}