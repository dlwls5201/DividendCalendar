package com.tistory.blackjinbase.ext

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("setHtmlText")
fun TextView.setHtmlText(htmlString: String?) {
    if (htmlString.isNullOrEmpty()) {
        return
    }
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlString)
    }
}
