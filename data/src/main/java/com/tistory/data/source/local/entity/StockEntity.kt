package com.tistory.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey
    val symbol: String,
    val stockCnt: Int = 0,
    val logoUrl: String = "",
    val companyName: String = ""
)