package com.tistory.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey
    val symbol: String,
    val stockCnt: Float = 0f,
    val logoUrl: String = "",
    val companyName: String = ""
)