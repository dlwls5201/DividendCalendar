package com.tistory.blackjinbase.ext.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes

interface AlertBuilder<out D : DialogInterface> {
    val ctx: Context

    var title: CharSequence

    @setparam:StringRes
    var titleResource: Int

    var message: CharSequence

    @setparam:StringRes
    var messageResource: Int

    fun onCancelled(handler: (dialog: DialogInterface) -> Unit)

    fun positiveButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit)
    fun positiveButton(
        @StringRes buttonTextResource: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    )

    fun negativeButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit)
    fun negativeButton(
        @StringRes buttonTextResource: Int,
        onClicked: (dialog: DialogInterface) -> Unit
    )

    fun build(): D
    fun show(): D
}

fun AlertBuilder<*>.okButton(handler: (dialog: DialogInterface) -> Unit) =
    positiveButton(android.R.string.ok, handler)

fun AlertBuilder<*>.cancelButton(handler: (dialog: DialogInterface) -> Unit) =
    negativeButton(android.R.string.cancel, handler)

fun AlertBuilder<*>.yesButton(handler: (dialog: DialogInterface) -> Unit) =
    positiveButton(android.R.string.yes, handler)

fun AlertBuilder<*>.noButton(handler: (dialog: DialogInterface) -> Unit) =
    negativeButton(android.R.string.no, handler)
