package com.tistory.dividendcalendar.presentation.calendar.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.tistory.blackjinbase.ext.toast
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ViewStockDialogBinding

fun Context.showStockDialog(
    confirmListener: (ticker: String, stockCnt: Int) -> Unit
): AlertDialog {
    val dialogView = DataBindingUtil.inflate<ViewStockDialogBinding>(
        LayoutInflater.from(this),
        R.layout.view_stock_dialog,
        null,
        false
    )

    val dialog = AlertDialog.Builder(this).apply {
        setView(dialogView.root)
        setCancelable(false)
    }

    val alertDialog = dialog.create()

    //init button
    with(dialogView) {
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnOk.setOnClickListener {
            val ticker = dialogView.etTicker.text.toString()
            var stockCount = dialogView.etStock.text.toString()

            if (ticker.isEmpty()) {
                toast(getString(R.string.input_ticker))
            } else {
                if (stockCount.isEmpty()) {
                    stockCount = "0"
                }
                confirmListener.invoke(ticker, stockCount.toInt())
            }
        }
    }

    return alertDialog
}