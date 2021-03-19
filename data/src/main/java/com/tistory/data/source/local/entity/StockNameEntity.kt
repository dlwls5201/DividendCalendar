package com.tistory.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ticker")
data class StockNameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ticker: String = "",
    val companyName: String = ""
)