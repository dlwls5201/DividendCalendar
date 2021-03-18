package com.tistory.blackjinbase.sample.simplelist

import android.os.Bundle
import android.view.View
import com.tistory.blackjinbase.R
import com.tistory.blackjinbase.base.BaseFragment
import com.tistory.blackjinbase.databinding.FragmentSimpeListViewBinding
import com.tistory.blackjinbase.sample.data.SampleData

class SimpleListRecyclerViewFragment :
    BaseFragment<FragmentSimpeListViewBinding>(R.layout.fragment_simpe_list_view) {

    override var logTag = "SimpleListRecyclerViewFragment"

    companion object {

        fun newInstance() = SimpleListRecyclerViewFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.items = SampleData.get("SimpleList")
        initButton()
    }

    private var shuffle = true

    private fun initButton() {
        binding.btnShuffle.setOnClickListener {
            if (shuffle) {
                binding.items = SampleData.getShuffle("SimpleList Shuffle")
            } else {
                binding.items = SampleData.get("SimpleList")
            }

            shuffle = !shuffle
        }
    }
}
