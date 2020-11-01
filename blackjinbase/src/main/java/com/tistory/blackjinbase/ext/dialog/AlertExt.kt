package com.tistory.blackjinbase.ext.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes

fun Context.alert(
    @StringRes messageResource: Int? = null,
    @StringRes titleResource: Int? = null,
    init: (AlertBuilder<DialogInterface>.() -> Unit)? = null
): AlertBuilder<DialogInterface> {
    return AndroidAlertBuilder(this).apply {
        if (titleResource != null) {
            this.titleResource = titleResource
        }
        if (messageResource != null) {
            this.messageResource = messageResource
        }
        if (init != null) init()
    }
}