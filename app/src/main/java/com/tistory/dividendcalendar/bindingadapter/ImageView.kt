package com.tistory.dividendcalendar.bindingadapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .placeholder(ColorDrawable(Color.GRAY))
        .error(ColorDrawable(Color.RED))
        .into(this)
}

@BindingAdapter("setCircleImageUrl")
fun ImageView.setCircleImageUrl(url: String?) {

    val options = RequestOptions().apply {
        circleCrop()
    }

    Glide.with(context)
        .load(url)
        .apply(options)
        .placeholder(ColorDrawable(Color.GRAY))
        .error(ColorDrawable(Color.DKGRAY))
        .into(this)
}