package com.tistory.blackjinbase.liverecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tistory.blackjinbase.BR

abstract class LiveRecyclerViewAdapter<VM : Any>(
    val lifecycleOwner: LifecycleOwner? = null,
    diffUtil: DiffUtil.ItemCallback<VM> = object : DiffUtil.ItemCallback<VM>() {
        override fun areItemsTheSame(oldItem: VM, newItem: VM): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: VM, newItem: VM): Boolean =
            areItemsTheSame(oldItem, newItem)
    }
) : ListAdapter<VM, LiveRecyclerViewHolder<VM>>(diffUtil) {

    @LayoutRes
    abstract fun getItemLayoutId(position: Int): Int

    final override fun getItemViewType(position: Int): Int = getItemLayoutId(position)

    /**
     * if viewModel Id is different. override this
     */
    protected val viewModelId: Int
        get() = BR.model

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LiveRecyclerViewHolder<VM> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            viewType,
            parent,
            false
        )

        return object : LiveRecyclerViewHolder<VM>(binding, lifecycleOwner) {
            override val viewModelId: Int = this@LiveRecyclerViewAdapter.viewModelId
        }
    }

    final override fun onBindViewHolder(holder: LiveRecyclerViewHolder<VM>, position: Int) {
        holder.bind(getItem(position))
    }

    final override fun onBindViewHolder(
        holder: LiveRecyclerViewHolder<VM>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }
}