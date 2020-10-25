package com.tistory.dividendcalendar.presentation.setting

import android.os.Bundle
import android.view.View
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.databinding.FragmentSettingBinding

class SettingFragment : DividendFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    companion object {

        fun newInstance() = SettingFragment()
    }

    override var logTag = "SettingFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}