package com.tistory.dividendcalendar.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tistory.dividendcalendar.presentation.model.ProfileItem

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val symbol: String,
    val logoUrl: String,
    val companyName: String,
    val exchange: String,
    val industry: String,
    val website: String,
    val description: String,
    val ceo: String,
    val securityName: String,
    val issueType: String,
    val sector: String,
    val primarySicCode: Int,
    val employees: Int,
    val address: String,
    val state: String,
    val city: String,
    val zip: String,
    val country: String,
    val phone: String
)

fun ProfileEntity.mapToItem() = ProfileItem(
    ticker = symbol,
    logoUrl = logoUrl,
    companyName = companyName
)

