package com.tistory.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tistory.domain.model.CalendarItem
import com.tistory.domain.model.DividendItem
import com.tistory.domain.model.Frequency

@Entity(
    tableName = "dividends"
    //ForeignKey.CASCADE 추가하면 stock 삭제가 아닌 수정만 해도 Dividends 모두 제거된다.
    //따라서 수동으로 모두 관리하도록 한다.
    /*foreignKeys = [
        ForeignKey(
            entity = StockEntity::class,
            parentColumns = arrayOf("symbol"),
            childColumns = arrayOf("parentSymbol"),
            onDelete = ForeignKey.CASCADE
        )]*/
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