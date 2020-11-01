package com.tistory.blackjinbase.ext.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

internal class AndroidAlertBuilder(override val ctx: Context) : AlertBuilder<AlertDialog> {
    private val builder = AlertDialog.Builder(ctx)

    override var title: CharSequence
        get() = throw Exception("Property does not have a getter")
        set(value) {
            builder.setTitle(value)
        }

    override var titleResource: Int
        get() = throw Exception("Property does not have a getter")
        set(value) {
            builder.setTitle(value)
        }

    override var message: CharSequence
        get() = throw Exception("Property does not have a getter")
        set(value) {
            builder.setMessage(value)
        }

    override var messageResource: Int
        get() = throw Exception("Property does not have a getter")
        set(value) {
            builder.setMessage(value)
        }

    override fun onCancelled(handler: (dialog: DialogInterface) -> Unit) {
        builder.setOnCancelListener(handler)
    }

    override fun positiveButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setPositiveButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    override fun positiveButton(
        buttonTextResource: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    ) {
        builder.setPositiveButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    override fun negativeButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNegativeButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    override fun negativeButton(
        buttonTextResource: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    ) {
        builder.setNegativeButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    override fun build(): AlertDialog = builder.create()

    override fun show(): AlertDialog = builder.show()
}