package com.tistory.data.source.remote.model


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("companyName")
    val companyName: String?,
    @SerializedName("exchange")
    val exchange: String?,
    @SerializedName("industry")
    val industry: String?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("CEO")
    val ceo: String?,
    @SerializedName("securityName")
    val securityName: String?,
    @SerializedName("issueType")
    val issueType: String?,
    @SerializedName("sector")
    val sector: String?,
    @SerializedName("primarySicCode")
    val primarySicCode: Int?,
    @SerializedName("employees")
    val employees: Int?,
    @SerializedName("tags")
    val tags: List<String>?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("address2")
    val address2: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("zip")
    val zip: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("phone")
    val phone: String?
)
