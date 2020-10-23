package com.tistory.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation


data class StockWithDividendEntity(
    @Embedded val stock: StockEntity,

    @Relation(
        parentColumn = "symbol",
        entityColumn = "parentSymbol"
    )
    val dividends: List<DividendEntity>
)