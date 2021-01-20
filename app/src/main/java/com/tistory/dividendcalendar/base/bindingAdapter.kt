package com.tistory.dividendcalendar.base

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:visibility")
fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}