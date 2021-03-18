package com.tistory.blackjinbase.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjinbase.simplerecyclerview.SimpleListAdapter

/**
 *  itemLayoutId="@{@layout/item_sample}"
 *  itemList="@{items}"
 */
@BindingAdapter("itemList", "itemLayoutId")
fun <VM : Any> RecyclerView.bindListAdapterData(
    itemList: List<VM>?,
    layoutId: Int
) {
    if (itemList == null) return

    if (layoutManager == null) {
        layoutManager = LinearLayoutManager(context)
    }

    val simpleLisAdapter = adapter as? SimpleListAdapter<VM>
        ?: object : SimpleListAdapter<VM>(
            layoutRes = layoutId
        ) {}

    adapter = simpleLisAdapter
    simpleLisAdapter.replaceAll(itemList)
}
