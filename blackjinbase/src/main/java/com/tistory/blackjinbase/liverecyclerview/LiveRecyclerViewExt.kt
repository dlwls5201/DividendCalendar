package com.tistory.blackjinbase.liverecyclerview

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun <VM : DiffComparable<VM>> RecyclerView.bindDiffComparable(
    itemList: List<VM>?,
    layoutId: Int,
    lifecycleOwner: LifecycleOwner?
) {
    if (itemList == null) return

    if (layoutManager == null) {
        layoutManager = LinearLayoutManager(context)
    }

    @Suppress("UNCHECKED_CAST")
    val adapter = adapter as? LiveRecyclerViewAdapter<VM>
        ?: (object : LiveRecyclerViewAdapter<VM>(
            lifecycleOwner,
            getDiffUtil()
        ) {
            override fun getItemLayoutId(position: Int) = layoutId
        }.also { adapter = it })

    adapter.submitList(itemList)
}

fun <VM : Any> RecyclerView.bindData(
    itemList: List<VM>?,
    layoutId: Int,
    lifecycleOwner: LifecycleOwner?,
    listener: ((item: VM) -> Unit)? = null
) {
    if (itemList == null) return

    if (layoutManager == null) {
        layoutManager = LinearLayoutManager(context)
    }

    @Suppress("UNCHECKED_CAST")
    val adapter = adapter as? LiveRecyclerViewAdapter<VM>
        ?: (object : LiveRecyclerViewAdapter<VM>(lifecycleOwner) {
            override fun getItemLayoutId(position: Int) = layoutId

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): LiveRecyclerViewHolder<VM> {
                return super.onCreateViewHolder(parent, viewType).apply {
                    itemView.setOnClickListener {
                        listener?.invoke(getItem(adapterPosition))
                    }
                }
            }

        }.also { adapter = it })

    adapter.submitList(itemList)
}

private fun <VM : DiffComparable<VM>> getDiffUtil(): DiffUtil.ItemCallback<VM> =
    object : DiffUtil.ItemCallback<VM>() {
        override fun areItemsTheSame(oldItem: VM, newItem: VM): Boolean =
            oldItem.areItemsTheSame(newItem)

        override fun areContentsTheSame(oldItem: VM, newItem: VM): Boolean =
            oldItem.areContentsTheSame(newItem)
    }