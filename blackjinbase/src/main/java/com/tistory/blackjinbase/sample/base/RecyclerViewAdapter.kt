package com.tistory.blackjinbase.sample.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.R
import com.tistory.blackjinbase.base.BaseViewHolder
import com.tistory.blackjinbase.databinding.ItemSampleBinding
import com.tistory.blackjinbase.simplerecyclerview.BaseDiffUtilCallback

class RecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder<ViewDataBinding, String>>() {

    private val items = mutableListOf<String>()

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(data: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewDataBinding, String> {
        return SampleViewHolder(parent).apply {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(
                    items[adapterPosition]
                )
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding, String>, position: Int) {
        holder.bind(items[position])
    }

    fun replaceAll(items: List<String>, useDiffCallback: Boolean = false) {
        val diffCallback = BaseDiffUtilCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.run {
            clear()
            addAll(items)
        }

        if (useDiffCallback) {
            diffResult.dispatchUpdatesTo(this)
        } else {
            notifyDataSetChanged()
        }
    }

    class SampleViewHolder(parent: ViewGroup) :
        BaseViewHolder<ItemSampleBinding, String>(parent, R.layout.item_sample) {

        override fun bind(data: String) {
            binding.model = data
        }

        override fun recycled() {}
    }
}