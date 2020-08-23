package com.tistory.dividendcalendar.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logos")
data class LogoEntity(
    @PrimaryKey val symbol: String,
    val logoUrl: String
)