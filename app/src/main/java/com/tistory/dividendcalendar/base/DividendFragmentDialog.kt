package com.tistory.dividendcalendar.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.tistory.blackjinbase.util.Showlog

abstract class DividendFragmentDialog<B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : DialogFragment(), BaseUi {

    protected lateinit var binding: B

    abstract var logTag: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewModelSetup()
    }

    override fun onViewModelSetup() {}

    override fun onResume() {
        super.onResume()
        Showlog.d(logTag)
    }
}