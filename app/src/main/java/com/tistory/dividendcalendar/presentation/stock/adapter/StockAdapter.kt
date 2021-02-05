package com.tistory.dividendcalendar.presentation.stock.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.base.BaseViewHolder
import com.tistory.blackjinbase.simplerecyclerview.BaseDiffUtilCallback
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ItemStockBinding
import com.tistory.domain.model.StockWithDividendItem

class StockAdapter :
    RecyclerView.Adapter<BaseViewHolder<ViewDataBinding, StockWithDividendItem>>() {

    private val items = mutableListOf<StockWithDividendItem>()

    var onStockClickListener: ((withDividendItem: StockWithDividendItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewDataBinding, StockWithDividendItem> {
        return StockViewHolder(parent).apply {
            itemView.setOnClickListener {
                val item = items[adapterPosition]
                onStockClickListener?.invoke(item)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewDataBinding, StockWithDividendItem>,
        position: Int
    ) {
        holder.bind(items[position])
    }

    fun replaceAll(withDividendItems: List<StockWithDividendItem>) {
        val diffCallback = BaseDiffUtilCallback(this.items, withDividendItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.run {
            clear()
            addAll(withDividendItems)
        }

        diffResult.dispatchUpdatesTo(this)
    }

    class StockViewHolder(parent: ViewGroup) :
        BaseViewHolder<ItemStockBinding, StockWithDividendItem>(parent, R.layout.item_stock) {

        override fun bind(data: StockWithDividendItem) {
            binding.item = data
        }

        override fun recycled() {}
    }
}