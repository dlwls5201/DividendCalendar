package com.tistory.blackjinbase.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("setCircleImageUrl")
fun ImageView.setCircleImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().circleCrop())
            .into(this)
    }
}