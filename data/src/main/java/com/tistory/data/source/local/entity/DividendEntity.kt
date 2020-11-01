package com.tistory.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tistory.domain.model.CalendarItem
import com.tistory.domain.model.DividendItem
import com.tistory.domain.model.Frequency

@Entity(
    tableName = "dividends",
    foreignKeys = [
        ForeignKey(
            entity = StockEntity::class,
            parentColumns = arrayOf("symbol"),
            childColumns = arrayOf("parentSymbol"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DividendEntity(
    @PrimaryKey
    val dividendId: String,
    val parentSymbol: String,
    val exDate: String = "",
    val declaredDate: String,
    val paymentDate: String = "",
    val recordDate: String = "",
    val amount: Float = 0f,
    val frequency: String? = ""
) {
    companion object {

        fun makeDividendId(symbol: String, paymentDate: String) = "${symbol}_${paymentDate}"
    }
}

fun DividendEntity.mapToItem(stock: StockEntity) = CalendarItem(
    ticker = parentSymbol,
    companyName = stock.companyName,
    logoUrl = stock.logoUrl,
    stockCnt = stock.stockCnt,
    exDate = exDate,
    paymentDate = paymentDate,
    declaredDate = declaredDate,
    amount = amount,
    frequency = Frequency.get(frequency)
)

fun DividendEntity.mapToItem() = DividendItem(
    ticker = parentSymbol,
    exDate = exDate,
    paymentDate = paymentDate,
    declaredDate = declaredDate,
    amount = amount,
    frequency = Frequency.get(frequency)
)