package com.tistory.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tistory.data.source.local.entity.StockEntity

@Dao
interface StockDao {

    @Query("SELECT * FROM stocks")
    suspend fun getStocks(): List<StockEntity>

    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStock(symbol: String): StockEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity)


    /*@Query("DELETE FROM stocks WHERE symbol = :symbol")
    suspend fun deleteProfileBySymbol(symbol: String)

    @Query("DELETE FROM stocks")
    suspend fun clearProfile()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDividend(dividend: DividendEntity)

    @Query("DELETE FROM dividends")
    suspend fun clearDividend()


    @Transaction
    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStockWithDividend(symbol: String): StockWithDividendEntity?

    @Transaction
    @Query("SELECT * FROM stocks")
    suspend fun getStockWithDividends(): List<StockWithDividendEntity>

    @Transaction
    @Query("SELECT * FROM stocks ORDER BY companyName ASC")
    suspend fun getSortingStockWithDividends(): List<StockWithDividendEntity>*/
}