package com.tistory.dividendcalendar.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.tistory.blackjinbase.base.BaseFragment

abstract class DividendFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : BaseFragment<B>(layoutId), BaseUi {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewModelSetup()
    }

    override fun onViewModelSetup() {}
}

interface BaseUi {
    fun onViewModelSetup()
}