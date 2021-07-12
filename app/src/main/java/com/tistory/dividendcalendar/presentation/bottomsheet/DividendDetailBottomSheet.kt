package com.tistory.dividendcalendar.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendBottomSheet
import com.tistory.dividendcalendar.databinding.BottomSheetDividendDetailBinding
import com.tistory.domain.base.BaseListener
import com.tistory.domain.model.QuoteItem
import com.tistory.domain.usecase.GetQuoteUsecase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DividendDetailBottomSheet : DividendBottomSheet<BottomSheetDividendDetailBinding>(R.layout.bottom_sheet_dividend_detail) {

    companion object {

        private const val ARGUMENT_TICKER = "ticker"

        fun newInstance(ticker: String) = DividendDetailBottomSheet().apply {
            arguments = bundleOf(ARGUMENT_TICKER to ticker)
        }
    }

    override var logTag = "DividendDetailBottomSheet"

    @Inject
    lateinit var getQuoteUsecase: GetQuoteUsecase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ticker = arguments?.getString(ARGUMENT_TICKER) ?: return
        Dlog.d("ticker : $ticker")

        lifecycleScope.launch {
            getQuoteUsecase.get(ticker, object : BaseListener<QuoteItem>() {
                override fun onSuccess(data: QuoteItem) {
                    binding.item = data
                    Dlog.d("data : $data")
                }

                override fun onLoading() {
                    showLoading()
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError : $error")
                    toast(error.message)
                    dismiss()
                }

                override fun onLoaded() {
                    hideLoading()
                }
            })
        }
    }

    private fun showLoading() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbLoading.visibility = View.GONE
    }
}
