package com.tistory.dividendcalendar.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendBottomSheet
import com.tistory.dividendcalendar.databinding.BottomSheetDividendDetailBinding
import com.tistory.domain.model.StockWithDividendItem

class DividendDetailBottomSheet : DividendBottomSheet<BottomSheetDividendDetailBinding>(R.layout.bottom_sheet_dividend_detail) {

    companion object {

        private const val ARGUMENT_STOCK = "stock"

        fun newInstance(item: StockWithDividendItem) = DividendDetailBottomSheet().apply {
            arguments = bundleOf(ARGUMENT_STOCK to item)
        }
    }

    override var logTag = "DividendDetailBottomSheet"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments?.getSerializable(ARGUMENT_STOCK)
        val item2 = arguments?.getParcelable(ARGUMENT_STOCK) as StockWithDividendItem?
        Dlog.d("item : $item")
        Dlog.d("item2 : $item2")
    }
}
