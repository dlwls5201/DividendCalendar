package com.tistory.dividendcalendar.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tistory.dividendcalendar.presentation.model.DividendItem
import com.tistory.dividendcalendar.presentation.model.Frequency

@Entity(tableName = "symbols")
data class SymbolEntity(
    @PrimaryKey(autoGenerate = true)
    val symbolId: Long = 0,
    val symbol: String
)

@Entity(tableName = "dividends")
data class DividendEntity(
    @PrimaryKey(autoGenerate = true)
    val dividendId: Long = 0,

    val declaredDate: String,
    val parentSymbol: String,

    val exDate: String,
    val paymentDate: String,
    val recordDate: String,
    val amount: Float,
    val frequency: String,
    val requestedDate: Long
)

data class SymbolWithDividends(
    @Embedded val symbol: SymbolEntity,

    @Relation(
        parentColumn = "symbolId",
        entityColumn = "dividendId"
    )
    val dividends: List<DividendEntity>
)

fun DividendEntity.mapToItem(companyName: String, logoUrl: String) = DividendItem(
    ticker = parentSymbol,
    companyName = companyName,
    logoUrl = logoUrl,

    declaredDate = declaredDate,
    exDate = exDate,
    paymentDate = paymentDate,
    recordDate = recordDate,
    amount = amount,
    frequency = Frequency.getFrequency(frequency)
)