package com.tistory.dividendcalendar.presentation.main.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StockModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val companyName: String = "",
    val companyLogo: String = "",
    val ticker: String = "",
    var amount: String = ""
)