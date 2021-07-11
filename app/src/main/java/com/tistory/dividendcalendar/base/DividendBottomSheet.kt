package com.tistory.dividendcalendar.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tistory.blackjinbase.util.Dlog
import com.tistory.blackjinbase.util.Showlog

abstract class DividendBottomSheet<B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : BottomSheetDialogFragment(), BaseUi {

    protected lateinit var binding: B

    abstract var logTag: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

    override fun show(manager: FragmentManager, mLogTag: String?) {
        try {
            super.show(manager, mLogTag)
        } catch (e: IllegalStateException) {
            Dlog.e(e.message)
        }
    }
}
