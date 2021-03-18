package com.tistory.blackjinbase.simplerecyclerview

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.BR

abstract class SimpleAsyncRecyclerViewAdapter<ITEM : Any, B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    private val bindingVariableId: Int? = BR.model
) : RecyclerView.Adapter<SimpleViewHolder<B>>() {

    private val asyncDiffer = AsyncListDiffer(this, BaseDiffItemCallback<ITEM>())

    fun replaceAll(items: List<ITEM>) {
        asyncDiffer.submitList(items)
    }

    fun getItem(position: Int) = asyncDiffer.currentList[position] ?: null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : SimpleViewHolder<B>(
            parent = parent,
            layoutRes = layoutRes,
            bindingVariableId = bindingVariableId
        ) {}

    override fun getItemCount(): Int = asyncDiffer.currentList.size

    override fun onBindViewHolder(holder: SimpleViewHolder<B>, position: Int) {
        holder.bind(asyncDiffer.currentList[position])
    }
}