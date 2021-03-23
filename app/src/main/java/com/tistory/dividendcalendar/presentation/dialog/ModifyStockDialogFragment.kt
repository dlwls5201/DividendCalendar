package com.tistory.dividendcalendar.presentation.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.tistory.blackjinbase.ext.hideSoftKeyBoard
import com.tistory.blackjinbase.ext.observeEvent
import com.tistory.blackjinbase.ext.toast
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragmentDialog
import com.tistory.dividendcalendar.constant.Constant
import com.tistory.dividendcalendar.databinding.DialogModifyStockBinding
import com.tistory.dividendcalendar.firebase.DWFirebaseAnalyticsLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyStockDialogFragment :
    DividendFragmentDialog<DialogModifyStockBinding>(R.layout.dialog_modify_stock) {

    companion object {

        const val ARGUMENT_TYPE = "type"

        const val ARGUMENT_TICKER = "ticker"
        const val ARGUMENT_STOCK_CNT = "stock_cnt"

        fun newInstanceForAdd() = ModifyStockDialogFragment().apply {
            arguments = bundleOf(ARGUMENT_TYPE to DialogType.ADD)
        }

        fun newInstanceForAdd(ticker: String) = ModifyStockDialogFragment().apply {
            arguments = bundleOf(
                ARGUMENT_TYPE to DialogType.ADD,
                ARGUMENT_TICKER to ticker
            )
        }

        fun newInstanceForModify(ticker: String, stockCnt: Float) =
            ModifyStockDialogFragment().apply {
                arguments = bundleOf(
                    Pair(ARGUMENT_TYPE, DialogType.MODIFY),
                    Pair(ARGUMENT_TICKER, ticker),
                    Pair(ARGUMENT_STOCK_CNT, stockCnt)
                )
            }
    }

    enum class DialogType {
        ADD, MODIFY
    }

    override var logTag = "ModifyStockDialogFragment"

    private val viewModel by viewModels<ModifyStockViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.modifyStockViewModel = viewModel
        showLog()
    }

    private fun showLog() {
        DWFirebaseAnalyticsLogger.sendScreen(Constant.FB_VIEW_STOCK_DIALOG)
    }

    override fun onViewModelSetup() {
        with(viewModel) {
            eventFinish.observeEvent(viewLifecycleOwner, {
                dismiss()
            })

            eventToast.observeEvent(viewLifecycleOwner, {
                toast(it)
            })

            eventHideKeyboard.observeEvent(viewLifecycleOwner, {
                requireActivity().hideSoftKeyBoard()
            })
        }
    }
}