package com.tistory.dividendcalendar.presentation.stock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.databinding.FragmentStockBinding
import com.tistory.dividendcalendar.presentation.bottomsheet.DividendDetailBottomSheet
import com.tistory.dividendcalendar.presentation.dialog.ModifyStockDialogFragment
import com.tistory.dividendcalendar.presentation.stock.adapter.StockAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stock.*

@AndroidEntryPoint
class StockFragment : DividendFragment<FragmentStockBinding>(R.layout.fragment_stock) {

    companion object {

        fun newInstance() = StockFragment()
    }

    override var logTag = "StockFragment"

    private val stockViewModel by viewModels<StockViewModel>()

    private val stockAdapter by lazy {
        StockAdapter().apply {
            onStockClickListener = {
                ModifyStockDialogFragment.newInstanceForModify(it.symbol, it.stockCnt)
                    .show(childFragmentManager, null)
            }
            onStockProfileClickListener = {
                DividendDetailBottomSheet.newInstance(it.symbol)
                    .show(childFragmentManager, null)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.stockViewModel = stockViewModel
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(rvStock) {
            adapter = stockAdapter
        }
    }

    override fun onViewModelSetup() {
        super.onViewModelSetup()

        stockViewModel.stockItems.observe(viewLifecycleOwner, {
            stockAdapter.replaceAll(it.sortedByDescending { item ->
                if (item.dividends.isNotEmpty()) {
                    item.dividends.first().paymentDate
                } else {
                    Char.MIN_VALUE.toString()
                }
            })
        })
    }
}