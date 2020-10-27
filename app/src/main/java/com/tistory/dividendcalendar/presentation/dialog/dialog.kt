package com.tistory.dividendcalendar.presentation.dialog

/*
fun Context.showStockDialog(
    confirmListener: (ticker: String, stockCnt: Int) -> Unit
): AlertDialog {
    val dialogView = DataBindingUtil.inflate<DialogModifyStockBinding>(
        LayoutInflater.from(this),
        R.layout.dialog_modify_stock,
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
                alertDialog.dismiss()
            }
        }
    }

    return alertDialog
}*/
