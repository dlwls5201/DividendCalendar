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

inline fun AlertBuilder<*>.okButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    positiveButton(android.R.string.ok, handler)

inline fun AlertBuilder<*>.cancelButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    negativeButton(android.R.string.cancel, handler)

inline fun AlertBuilder<*>.yesButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    positiveButton(android.R.string.yes, handler)

inline fun AlertBuilder<*>.noButton(noinline handler: (dialog: DialogInterface) -> Unit) =
    negativeButton(android.R.string.no, handler)
