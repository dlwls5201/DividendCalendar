package com.tistory.blackjinbase.sample.simpleasync

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tistory.blackjinbase.BR
import com.tistory.blackjinbase.R
import com.tistory.blackjinbase.base.BaseFragment
import com.tistory.blackjinbase.databinding.FragmentRecyclerViewBinding
import com.tistory.blackjinbase.databinding.ItemSampleBinding
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.sample.data.SampleData
import com.tistory.blackjinbase.simplerecyclerview.SimpleAsyncRecyclerViewAdapter
import com.tistory.blackjinbase.simplerecyclerview.SimpleViewHolder

class SimpleAsyncRecyclerViewFragment :
    BaseFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    override var logTag = "SimpleAsyncRecyclerViewFragment"

    companion object {

        fun newInstance() = SimpleAsyncRecyclerViewFragment()
    }

    private val simpleAdapter by lazy {
        object : SimpleAsyncRecyclerViewAdapter<String, ItemSampleBinding>(
            R.layout.item_sample,
            BR.model
        ) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): SimpleViewHolder<ItemSampleBinding> {
                return super.onCreateViewHolder(parent, viewType).apply {
                    itemView.setOnClickListener {
                        val data = getItem(adapterPosition)
                        toast(data)
                    }
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
                simpleAdapter.replaceAll(SampleData.getShuffle("SimpleAsync Shuffle"))
            } else {
                simpleAdapter.replaceAll(SampleData.get("SimpleAsync"))
            }

            shuffle = !shuffle
        }
    }

    private fun initRecyclerView() {
        binding.listSample.adapter = simpleAdapter
        simpleAdapter.replaceAll(SampleData.get("SimpleAsync"))
    }
}
