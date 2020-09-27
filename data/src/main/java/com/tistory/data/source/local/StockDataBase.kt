package com.tistory.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tistory.data.source.local.entity.DividendEntity
import com.tistory.data.source.local.entity.StockEntity

@Database(
    entities = [StockEntity::class, DividendEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StockDataBase : RoomDatabase() {

    abstract fun getStockDao(): StockDao

    companion object {

        private const val DB_NAME = "stockDataBase.db"

        private var INSTANCE: StockDataBase? = null

        fun getInstance(context: Context): StockDataBase {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context, StockDataBase::class.java, DB_NAME
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
