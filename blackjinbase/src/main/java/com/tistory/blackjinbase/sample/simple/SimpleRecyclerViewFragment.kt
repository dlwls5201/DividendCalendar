package com.tistory.blackjinbase.sample.simple

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
import com.tistory.blackjinbase.simplerecyclerview.SimpleRecyclerViewAdapter
import com.tistory.blackjinbase.simplerecyclerview.SimpleViewHolder

class SimpleRecyclerViewFragment :
    BaseFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    override var logTag = "SimpleRecyclerViewFragment"

    companion object {

        fun newInstance() = SimpleRecyclerViewFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.listSample) {
            adapter = object : SimpleRecyclerViewAdapter<String, ItemSampleBinding>(
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
                            this@SimpleRecyclerViewFragment.requireContext().toast(data)
                        }
                    }
                }
            }.apply {
                replaceAll(SampleData.get("SimpleRecyclerViewFragment"))
            }
        }
    }
}