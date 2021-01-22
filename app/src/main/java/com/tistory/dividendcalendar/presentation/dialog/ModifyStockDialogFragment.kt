package com.tistory.dividendcalendar.presentation.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.tistory.blackjinbase.ext.hideSoftKeyBoard
import com.tistory.blackjinbase.ext.toast
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragmentDialog
import com.tistory.dividendcalendar.databinding.DialogModifyStockBinding
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
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.modifyStockViewModel = viewModel
    }

    override fun onViewModelSetup() {
        with(viewModel) {
            eventFinish.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let {
                    dismiss()
                }
            })

            eventToast.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let { message ->
                    requireContext().toast(message)
                }
            })

            eventHideKeyboard.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let {
                    requireActivity().hideSoftKeyBoard()
                }
            })
        }
    }
}