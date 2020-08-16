package com.tistory.dividendcalendar.base.simplerecyclerview

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleRecyclerViewAdapter<ITEM : Any, B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    private val bindingVariableId: Int? = null
) : RecyclerView.Adapter<SimpleViewHolder<B>>() {

    private val items = mutableListOf<ITEM>()

    fun replaceAll(items: List<ITEM>?) {
        items?.let {
            this.items.run {
                clear()
                addAll(it)
            }
            notifyDataSetChanged()
        }
    }

    fun getItem(position: Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : SimpleViewHolder<B>(
            layoutRes = layoutRes,
            parent = parent,
            bindingVariableId = bindingVariableId
        ) {}

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleViewHolder<B>, position: Int) {
        holder.bind(items[position])
    }
}