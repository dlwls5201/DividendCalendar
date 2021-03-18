package com.tistory.blackjinbase.listener

import android.view.View

abstract class OnSingleClickListener(
    private val clickDelayMilliSecond: Long = CLICK_DELAY_TIME
) : View.OnClickListener {

    companion object {
        private const val CLICK_DELAY_TIME = 1000L
    }

    private var lastClickTime = 0L

    protected abstract fun onSingleClick(view: View)

    override fun onClick(view: View) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime > clickDelayMilliSecond) {
            onSingleClick(view)
            lastClickTime = now
        }
    }
}