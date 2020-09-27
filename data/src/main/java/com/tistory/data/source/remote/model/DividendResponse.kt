package com.tistory.data.source.remote.model


import com.google.gson.annotations.SerializedName

data class DividendResponse(
    @SerializedName("exDate")
    val exDate: String,
    @SerializedName("paymentDate")
    val paymentDate: String,
    @SerializedName("recordDate")
    val recordDate: String,
    @SerializedName("declaredDate")
    val declaredDate: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("flag")
    val flag: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("frequency")
    val frequency: String //monthly, quarterly, semi-annual, annual

)
