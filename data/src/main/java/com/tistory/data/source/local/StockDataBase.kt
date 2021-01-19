package com.tistory.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tistory.data.source.local.entity.DividendEntity
import com.tistory.data.source.local.entity.StockEntity

@Database(
    entities = [StockEntity::class, DividendEntity::class],
    version = 3,
    exportSchema = false
)
abstract class StockDataBase : RoomDatabase() {

    abstract fun getStockDao(): StockDao

    companion object {

        private const val DB_NAME = "stockDataBase.db"

        private var INSTANCE: StockDataBase? = null

        fun getInstance(context: Context): StockDataBase {
            if (INSTANCE == null) {
                /**
                 * Android ROOM is based on SQL Lite which does not allow dropping database table columns. Instead you have to:
                 *
                 * - create a temporary table without the column you are trying to delete,
                 * - copy all records from old table to new table,
                 * - drop the old table,
                 * - rename new table to same name as old table
                 */
                val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE backup_table (symbol TEXT PRIMARY KEY NOT NULL, stockCnt INTEGER NOT NULL, logoUrl TEXT NOT NULL, companyName TEXT NOT NULL)")
                        database.execSQL("INSERT INTO backup_table(symbol, stockCnt, logoUrl, companyName) SELECT symbol, stockCnt, logoUrl, companyName FROM stocks")
                        database.execSQL("DROP TABLE stocks")
                        database.execSQL("ALTER TABLE backup_table RENAME TO stocks")
                    }
                }

                val MIGRATION_2_3 = object : Migration(2, 3) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE backup_table (symbol TEXT PRIMARY KEY NOT NULL, stockCnt FLOAT NOT NULL, logoUrl TEXT NOT NULL, companyName TEXT NOT NULL)")
                        database.execSQL("INSERT INTO backup_table(symbol, stockCnt, logoUrl, companyName) SELECT symbol, stockCnt, logoUrl, companyName FROM stocks")
                        database.execSQL("DROP TABLE stocks")
                        database.execSQL("ALTER TABLE backup_table RENAME TO stocks")
                    }
                }

                synchronized(RoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context, StockDataBase::class.java, DB_NAME
                    ).addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
