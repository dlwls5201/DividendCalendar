package com.tistory.dividendcalendar.bindingadapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.tistory.blackjinbase.util.Dlog
import java.util.*

@BindingAdapter("bind:changeTextColor")
fun TextView.changeTextColor(query: String) {
    val currentText = text.toString()
    if (currentText.length >= query.length) {
        val upperText = currentText.toUpperCase(Locale.getDefault())
        val upperQuery = query.toUpperCase(Locale.getDefault())
        val firstIndex = upperText.indexOf(upperQuery)
        if (firstIndex < 0) {
            return
        }

        val ssb = SpannableStringBuilder(currentText)
        val lastIndex = firstIndex + (query.length)
        Dlog.d("query : $query , currentText : $currentText , firstIndex : $firstIndex , lastIndex : $lastIndex")

        ssb.setSpan(
            ForegroundColorSpan(Color.parseColor("#0D47A1")), firstIndex, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = ssb
    }
}