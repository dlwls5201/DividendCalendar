package com.tistory.blackjinbase.sample

import android.os.Bundle
import android.view.View
import com.tistory.blackjinbase.R
import com.tistory.blackjinbase.base.BaseFragment
import com.tistory.blackjinbase.databinding.FragmentSampleBinding
import com.tistory.blackjinbase.ext.alert
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.listener.OnSingleClickListener

class SampleFragment : BaseFragment<FragmentSampleBinding>(R.layout.fragment_sample) {

    override var logTag = "SampleFragment"

    companion object {

        fun newInstance() = SampleFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnAlert.setOnClickListener {
                requireContext().alert(title = "BlackJin", message = "Welcome to my blog") {
                    positiveButton("ok") {
                        requireContext().toast("click the positive button")
                        //auto dismiss
                    }

                    negativeButton("cancel") {
                        //auto dismiss
                    }
                }.show()
            }

            var count = 0
            btnSingleClick.setOnClickListener(object : OnSingleClickListener(3000) {
                override fun onSingleClick(view: View) {
                    btnSingleClick.text = "${count++}"
                }
            })

            btnRecyclerView.setOnClickListener {
                (requireActivity() as SampleActivity).goToRecyclerViewFragment()
            }

            btnSimpleRecyclerView.setOnClickListener {
                (requireActivity() as SampleActivity).goToSimpleRecyclerViewFragment()
            }

            btnLiveRecyclerView.setOnClickListener {
                (requireActivity() as SampleActivity).goToLiveRecyclerViewFragment()
            }
        }
    }
}