package com.tistory.dividendcalendar.presentation.stock

import android.os.Bundle
import android.view.View
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.databinding.FragmentStockBinding

class StockFragment : DividendFragment<FragmentStockBinding>(R.layout.fragment_stock) {

    companion object {

        fun newInstance() = StockFragment()
    }

    override var logTag = "StockFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}