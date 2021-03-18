package com.tistory.blackjinbase.simplerecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.BR

abstract class SimpleListAdapter<ITEM : Any>(
    @LayoutRes private val layoutRes: Int,
    private val bindingVariableId: Int? = BR.model,
    diffUtil: DiffUtil.ItemCallback<ITEM> = object : DiffUtil.ItemCallback<ITEM>() {
        override fun areItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean =
            areItemsTheSame(oldItem, newItem)
    }
) : ListAdapter<ITEM, SimpleListViewHolder>(diffUtil) {

    fun replaceAll(items: List<ITEM>) {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : SimpleListViewHolder(
            parent = parent,
            layoutRes = layoutRes,
            bindingVariableId = bindingVariableId
        ) {}

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holderList: SimpleListViewHolder, position: Int) {
        holderList.bind(currentList[position])
    }
}

abstract class SimpleListViewHolder(
    parent: ViewGroup?,
    @LayoutRes layoutRes: Int,
    private val bindingVariableId: Int?
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent?.context)
        .inflate(layoutRes, parent, false)
) {

    protected val binding: ViewDataBinding = DataBindingUtil.bind(itemView)!!

    fun bind(item: Any?) {
        try {
            binding.run {
                bindingVariableId?.let {
                    setVariable(it, item)
                }
                executePendingBindings()
            }
            itemView.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
            itemView.visibility = View.GONE
        }
    }
}