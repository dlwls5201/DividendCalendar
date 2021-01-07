package com.tistory.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.tistory.domain.model.StockWithDividendItem


data class StockWithDividendEntity(
    @Embedded val stock: StockEntity,

    @Relation(
        parentColumn = "symbol",
        entityColumn = "parentSymbol"
    )
    val dividends: List<DividendEntity>
)

fun StockWithDividendEntity.mapToStockItem() = StockWithDividendItem(
    symbol = stock.symbol,
    stockCnt = stock.stockCnt,
    logoUrl = stock.logoUrl,
    companyName = stock.companyName,
    dividends = dividends.sortedByDescending { it.paymentDate }.map { it.mapToItem() }
)