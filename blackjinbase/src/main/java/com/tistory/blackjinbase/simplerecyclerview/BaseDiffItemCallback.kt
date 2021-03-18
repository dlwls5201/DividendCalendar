package com.tistory.blackjinbase.simplerecyclerview

import androidx.recyclerview.widget.DiffUtil

class BaseDiffItemCallback<ITEM : Any> : DiffUtil.ItemCallback<ITEM>() {
    override fun areItemsTheSame(oldItem: ITEM, newItem: ITEM) = oldItem == newItem

    override fun areContentsTheSame(oldItem: ITEM, newItem: ITEM) =
        areItemsTheSame(oldItem, newItem)
}