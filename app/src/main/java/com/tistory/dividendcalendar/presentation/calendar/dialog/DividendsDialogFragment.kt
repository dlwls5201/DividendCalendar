package com.tistory.dividendcalendar.presentation.calendar.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.tistory.blackjinbase.simplerecyclerview.SimpleRecyclerViewAdapter
import com.tistory.blackjinbase.simplerecyclerview.SimpleViewHolder
import com.tistory.dividendcalendar.BR
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.DialogDividendsBinding
import com.tistory.dividendcalendar.databinding.ItemDividendBinding
import com.tistory.domain.model.CalendarItem
import kotlinx.android.synthetic.main.dialog_dividends.*

class DividendsDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "DividendsDialogFragment"

        private const val ARGUMENT_DIVIDEND_ITEMS = "dividend_items"

        fun newInstance(items: List<CalendarItem>) = DividendsDialogFragment().apply {
            arguments = bundleOf(Pair(ARGUMENT_DIVIDEND_ITEMS, items.toTypedArray()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DialogDividendsBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = arguments?.getParcelableArray(ARGUMENT_DIVIDEND_ITEMS)
            ?.toList() as List<CalendarItem>

        with(rvDialogDividends) {
            adapter = object : SimpleRecyclerViewAdapter<CalendarItem, ItemDividendBinding>(
                R.layout.item_dividend,
                BR.item
            ) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): SimpleViewHolder<ItemDividendBinding> {
                    return super.onCreateViewHolder(parent, viewType).apply {
                        itemView.setOnClickListener {
                        }
                    }
                }
            }.apply {
                replaceAll(items)
            }
        }
    }
}