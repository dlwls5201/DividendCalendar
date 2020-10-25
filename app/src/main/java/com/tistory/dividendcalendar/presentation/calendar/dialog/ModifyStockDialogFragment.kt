package com.tistory.dividendcalendar.presentation.calendar.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.DialogModifyStockBinding
import com.tistory.dividendcalendar.di.Injection
import com.tistory.domain.base.BaseListener
import kotlinx.android.synthetic.main.dialog_modify_stock.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModifyStockDialogFragment : DialogFragment() {

    companion object {

        private const val ARGUMENT_TYPE = "type"

        private const val ARGUMENT_TICKER = "ticker"
        private const val ARGUMENT_STOCK_CNT = "stock_cnt"

        fun newInstanceForAdd() = ModifyStockDialogFragment().apply {
            arguments = bundleOf(ARGUMENT_TYPE to DialogType.ADD)
        }

        fun newInstanceForModify(ticker: String, stockCnt: Int) =
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

    private val addStockUsecase by lazy {
        Injection.provideAddStockUsecase()
    }

    private val repository by lazy {
        Injection.provideStockWithDividendRepo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DialogModifyStockBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewByType()
        initButton()
    }

    private fun initViewByType() {
        val dialogType = arguments?.getSerializable(ARGUMENT_TYPE) as DialogType?
        when (dialogType) {
            DialogType.ADD -> {
                btnDelete.visibility = View.GONE
            }
            DialogType.MODIFY -> {
                btnDelete.visibility = View.VISIBLE

                val ticker = arguments?.getString(ARGUMENT_TICKER, "") ?: ""
                val stockCnt = arguments?.getInt(ARGUMENT_STOCK_CNT, 0) ?: 0

                etTicker.setText(ticker)
                etTicker.isEnabled = false
                etStockCnt.setText(stockCnt.toString())
            }
            null -> {
                //..
            }
        }
    }

    private fun initButton() {
        btnCancel.setOnClickListener {
            dismiss()
        }

        btnDelete.setOnClickListener {
            val ticker = etTicker.text.toString()
            deleteStock(ticker)
        }

        btnOk.setOnClickListener {
            val ticker = etTicker.text.toString()
            var stockCount = etStockCnt.text.toString()

            if (ticker.isEmpty()) {
                requireContext().toast(getString(R.string.input_ticker))
            } else {
                if (stockCount.isEmpty()) {
                    stockCount = "0"
                }

                putStockWithDividend(ticker, stockCount.toInt())
                hideKeyboard(etStockCnt)
            }
        }
    }

    private fun hideKeyboard(editText: EditText) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
        view?.clearFocus()
    }

    private fun deleteStock(ticker: String) {
        lifecycleScope.launch {
            repository.deleteStock(ticker)
            with(Dispatchers.Main) {
                dismiss()
            }
        }
    }

    private fun putStockWithDividend(ticker: String, stockCnt: Int) {
        lifecycleScope.launch {
            addStockUsecase.build(ticker, stockCnt, object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                }

                override fun onLoading() {
                    Dlog.d("onLoading")
                    showProgress()
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError : ${error.message}")
                }

                override fun onLoaded() {
                    Dlog.d("onLoaded")
                    hideProgress()
                    dismiss()
                }
            })
        }
    }

    private fun showProgress() {
        flProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        flProgress.visibility = View.GONE
    }
}