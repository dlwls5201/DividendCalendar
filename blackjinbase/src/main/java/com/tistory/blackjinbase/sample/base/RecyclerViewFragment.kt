package com.tistory.blackjinbase.sample.base

import android.os.Bundle
import android.view.View
import com.tistory.blackjinbase.R
import com.tistory.blackjinbase.base.BaseFragment
import com.tistory.blackjinbase.databinding.FragmentRecyclerViewBinding
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.sample.data.SampleData

class RecyclerViewFragment :
    BaseFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    override var logTag = "RecyclerViewFragment"

    companion object {

        fun newInstance() = RecyclerViewFragment()
    }

    private val recyclerViewAdapter by lazy {
        RecyclerViewAdapter().apply {
            onItemClickListener = object :
                RecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(data: String) {
                    this@RecyclerViewFragment.requireContext().toast(data)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        initRecyclerView()
    }

    private var shuffle = true

    private fun initButton() {
        binding.btnShuffle.setOnClickListener {
            if (shuffle) {
                recyclerViewAdapter.replaceAll(SampleData.getShuffle("Base Shuffle"))
            } else {
                recyclerViewAdapter.replaceAll(SampleData.get("Base"))
            }

            shuffle = !shuffle
        }
    }

    private fun initRecyclerView() {
        binding.listSample.adapter = recyclerViewAdapter
        recyclerViewAdapter.replaceAll(SampleData.get("Base"))
    }
}