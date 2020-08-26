package com.tistory.dividendcalendar.data.source.remote.model


import com.google.gson.annotations.SerializedName
import com.tistory.dividendcalendar.data.source.local.entity.ProfileEntity

data class ProfileResponse(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("industry")
    val industry: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("CEO")
    val ceo: String,
    @SerializedName("securityName")
    val securityName: String,
    @SerializedName("issueType")
    val issueType: String,
    @SerializedName("sector")
    val sector: String,
    @SerializedName("primarySicCode")
    val primarySicCode: Int,
    @SerializedName("employees")
    val employees: Int,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("address")
    val address: String,
    @SerializedName("address2")
    val address2: String?,
    @SerializedName("state")
    val state: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("zip")
    val zip: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("phone")
    val phone: String
)

fun ProfileResponse.mapToEntity(logoUrl: String) = ProfileEntity(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    exchange = exchange,
    industry = industry,
    website = website,
    description = description,
    ceo = ceo,
    securityName = securityName,
    issueType = issueType,
    sector = sector,
    primarySicCode = primarySicCode,
    employees = employees,
    address = address,
    state = state,
    city = city,
    zip = zip,
    country = country,
    phone = phone
)