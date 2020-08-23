package com.tistory.dividendcalendar.data.source.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tistory.dividendcalendar.DividendCalendarApplication
import com.tistory.dividendcalendar.data.source.local.entity.DividendEntity
import com.tistory.dividendcalendar.data.source.local.entity.LogoEntity
import com.tistory.dividendcalendar.data.source.local.entity.SymbolEntity

@Database(
    entities = [LogoEntity::class, SymbolEntity::class, DividendEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StockDataBase : RoomDatabase() {

    abstract fun getStockDao(): StockDao

    companion object {

        private var INSTANCE: StockDataBase? = null

        fun getInstance(): StockDataBase {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        DividendCalendarApplication.INSTANCE,
                        StockDataBase::class.java,
                        "stockDataBase.db"
                    ).fallbackToDestructiveMigration()  //TODO 테스트용 : 빌드 시 마다 기존 데이터베이스 삭제
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}