package com.tistory.dividendcalendar

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.tistory.blackjinbase.base.BaseActivity
import com.tistory.dividendcalendar.base.BaseUi

abstract class DividendActivity<B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : BaseActivity<B>(layoutId), BaseUi {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onViewModelSetup()
    }

    override fun onViewModelSetup() {}
}