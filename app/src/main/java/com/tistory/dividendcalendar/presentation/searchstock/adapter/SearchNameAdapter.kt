package com.tistory.dividendcalendar.presentation.searchstock.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.base.BaseViewHolder
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ItemStockNameBinding

class SearchNameAdapter :
    RecyclerView.Adapter<BaseViewHolder<ViewDataBinding, StockNameItem>>() {

    private val items = mutableListOf<StockNameItem>()

    var onItemClickListener: ((item: StockNameItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewDataBinding, StockNameItem> {
        return TickerViewHolder(parent).apply {
            itemView.setOnClickListener {
                val item = items[adapterPosition]
                onItemClickListener?.invoke(item)
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewDataBinding, StockNameItem>, position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun replaceAll(items: List<StockNameItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class TickerViewHolder(parent: ViewGroup) :
        BaseViewHolder<ItemStockNameBinding, StockNameItem>(parent, R.layout.item_stock_name) {

        override fun bind(data: StockNameItem) {
            binding.item = data
        }
    }
}