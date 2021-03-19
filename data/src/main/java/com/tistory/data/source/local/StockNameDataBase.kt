package com.tistory.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tistory.data.source.local.entity.StockNameEntity

@Database(
    entities = [StockNameEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StockNameDataBase : RoomDatabase() {

    abstract fun getTickerDao(): StockNameDao

    companion object {

        private const val DB_NAME = "stockNameDatabase.db"

        private var INSTANCE: StockNameDataBase? = null

        fun getInstance(context: Context): StockNameDataBase {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context, StockNameDataBase::class.java, DB_NAME
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
