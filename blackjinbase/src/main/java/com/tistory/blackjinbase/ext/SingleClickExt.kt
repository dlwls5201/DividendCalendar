package com.tistory.blackjinbase.ext

import android.view.View
import com.tistory.blackjinbase.listener.OnSingleClickListener

fun View.setOnSingleClickListener(action: (v: View) -> Unit) {
    setOnClickListener(object : OnSingleClickListener() {
        override fun onSingleClick(view: View) {
            action.invoke(view)
        }
    })
}

fun View.setOnSingleClickListener(action: (v: View) -> Unit, interval: Long) {
    setOnClickListener(object : OnSingleClickListener(interval) {
        override fun onSingleClick(view: View) {
            action.invoke(view)
        }
    })
}