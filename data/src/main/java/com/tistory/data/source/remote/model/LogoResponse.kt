package com.tistory.data.source.remote.model


import com.google.gson.annotations.SerializedName

data class LogoResponse(
    @SerializedName("url")
    val url: String = ""
)
