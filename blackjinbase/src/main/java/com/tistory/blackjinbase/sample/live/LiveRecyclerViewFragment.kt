package com.tistory.blackjinbase.sample.live

import android.os.Bundle
import android.view.View
import com.tistory.blackjinbase.R
import com.tistory.blackjinbase.base.BaseFragment
import com.tistory.blackjinbase.databinding.FragmentRecyclerViewBinding
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.liverecyclerview.bindData
import com.tistory.blackjinbase.sample.data.SampleData

class LiveRecyclerViewFragment :
    BaseFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    override var logTag = "LiveRecyclerViewFragment"

    companion object {

        fun newInstance() = LiveRecyclerViewFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listSample.bindData(
            SampleData.get("LiveRecyclerViewFragment"),
            R.layout.item_sample,
            viewLifecycleOwner
        ) {
            requireContext().toast(it)
        }
    }
}