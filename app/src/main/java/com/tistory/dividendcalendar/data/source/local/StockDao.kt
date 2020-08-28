package com.tistory.dividendcalendar.data.source.local

import androidx.room.*
import com.tistory.dividendcalendar.data.source.local.entity.DividendEntity
import com.tistory.dividendcalendar.data.source.local.entity.StockEntity
import com.tistory.dividendcalendar.data.source.local.entity.StockWithDividendEntity

@Dao
interface StockDao {

    //stock
    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStock(symbol: String): StockEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity)

    @Query("DELETE FROM stocks WHERE symbol = :symbol")
    suspend fun deleteProfileBySymbol(symbol: String)

    @Query("DELETE FROM stocks")
    suspend fun clearProfile()


    //dividend
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDividend(dividend: DividendEntity)

    @Query("DELETE FROM dividends")
    suspend fun clearDividend()


    //stockWithDividend
    @Transaction
    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStockWithDividend(symbol: String): StockWithDividendEntity?

    @Transaction
    @Query("SELECT * FROM stocks")
    suspend fun getStockWithDividends(): List<StockWithDividendEntity>

    @Transaction
    @Query("SELECT * FROM stocks ORDER BY companyName ASC")
    suspend fun getSortingStockWithDividends(): List<StockWithDividendEntity>
}