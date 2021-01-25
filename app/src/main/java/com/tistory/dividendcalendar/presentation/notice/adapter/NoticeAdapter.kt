package com.tistory.dividendcalendar.presentation.notice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ItemNoticeBinding
import com.tistory.dividendcalendar.presentation.notice.model.NoticeItem

class NoticeAdapter : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private val items = mutableListOf<NoticeItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        return NoticeViewHolder(parent).apply {
            itemView.setOnClickListener {

                if (binding == null) {
                    return@setOnClickListener
                }

                val item = getItem(adapterPosition)
                val expandable = item.expandable
                Dlog.d("expandable : $expandable")

                if (expandable) {
                    binding.expandableLayout.collapse()
                    binding.ivExpandable.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_arrow_bottom
                        )
                    )
                } else {
                    binding.expandableLayout.expand()
                    binding.ivExpandable.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_arrow_top
                        )
                    )
                }

                val updateItem = item.copy(expandable = expandable.not())
                changeItem(adapterPosition, updateItem)
            }
        }
    }

    private fun getItem(position: Int) = items[position]

    private fun changeItem(position: Int, item: NoticeItem) {
        items[position] = item
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun replaceAll(items: List<NoticeItem>?) {
        items?.let {
            this.items.run {
                clear()
                addAll(it)
            }
            notifyDataSetChanged()
        }
    }

    class NoticeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
    ) {

        val binding: ItemNoticeBinding? = DataBindingUtil.bind(itemView)

        fun bind(item: NoticeItem) {
            binding?.model = item
        }
    }
}