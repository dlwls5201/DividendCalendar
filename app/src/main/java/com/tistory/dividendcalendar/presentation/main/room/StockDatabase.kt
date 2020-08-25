package com.tistory.dividendcalendar.presentation.main.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tistory.dividendcalendar.presentation.main.model.StockModel

@Database(entities = [StockModel::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}