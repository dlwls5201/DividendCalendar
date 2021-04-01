package com.tistory.dividendcalendar.bindingadapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tistory.blackjinbase.ext.safeLet
import com.tistory.blackjinbase.ext.toPx
import com.tistory.dividendcalendar.R
import java.util.*

@BindingAdapter("bind:changeTextColor")
fun TextView.changeTextColor(query: String) {
    val currentText = text.toString()
    if (TextUtils.isEmpty(currentText)) {
        return
    }

    if (currentText.length >= query.length) {
        val upperText = currentText.toUpperCase(Locale.getDefault())
        val upperQuery = query.toUpperCase(Locale.getDefault())
        val firstIndex = upperText.indexOf(upperQuery)
        if (firstIndex < 0) {
            return
        }

        val ssb = SpannableStringBuilder(currentText)
        val lastIndex = firstIndex + (query.length)
        //Dlog.d("query : $query , currentText : $currentText , firstIndex : $firstIndex , lastIndex : $lastIndex")

        ssb.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.colorPrimary)
            ), firstIndex, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = ssb
    }
}

@BindingAdapter("bind:setImageUrl", "bind:setTickerName")
fun ImageView.setImageUrlWithTicker(url: String?, ticker: String?) {
    safeLet(url, ticker) { _url, _ticker ->

        val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val circlePaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.colorPrimary)
        }

        canvas.drawArc(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), 0f, 360f, true, circlePaint)

        val textPaint = Paint().apply {
            isAntiAlias = true
            textSize = 30.toPx().toFloat()
            color = ContextCompat.getColor(context, R.color.white)
            textAlign = Paint.Align.CENTER
        }

        val xPos = canvas.width / 2
        val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()

        canvas.drawText("${_ticker.first()}", xPos.toFloat(), yPos.toFloat(), textPaint)

        val drawable = BitmapDrawable(resources, bitmap)

        if (ticker == "NKE") {
            setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.logo_nike)
            )
        } else {
            Glide.with(context)
                .load(_url)
                .placeholder(drawable)
                .into(this)
        }
    }
}
