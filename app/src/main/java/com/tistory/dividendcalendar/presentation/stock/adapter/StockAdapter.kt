package com.tistory.dividendcalendar.presentation.stock.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.base.BaseDiffUtilCallback
import com.tistory.blackjinbase.base.BaseViewHolder
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ItemStockBinding
import com.tistory.domain.model.StockItem

class StockAdapter : RecyclerView.Adapter<BaseViewHolder<ViewDataBinding, StockItem>>() {

    private val items = mutableListOf<StockItem>()

    var onStockClickListener: ((item: StockItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewDataBinding, StockItem> {
        return StockViewHolder(parent).apply {
            itemView.setOnClickListener {
                val item = items[adapterPosition]
                onStockClickListener?.invoke(item)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewDataBinding, StockItem>,
        position: Int
    ) {
        holder.bind(items[position])
    }

    fun replaceAll(items: List<StockItem>) {
        val diffCallback = BaseDiffUtilCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.run {
            clear()
            addAll(items)
        }

        diffResult.dispatchUpdatesTo(this)
    }

    class StockViewHolder(parent: ViewGroup) :
        BaseViewHolder<ItemStockBinding, StockItem>(parent, R.layout.item_stock) {

        override fun bind(data: StockItem) {
            binding.item = data
        }

        override fun recycled() {}
    }
}