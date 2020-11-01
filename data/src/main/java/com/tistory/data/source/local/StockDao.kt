package com.tistory.data.source.local

import androidx.room.*
import com.tistory.data.source.local.entity.DividendEntity
import com.tistory.data.source.local.entity.StockEntity
import com.tistory.data.source.local.entity.StockWithDividendEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Query("SELECT * FROM stocks")
    fun getStocks(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks")
    fun getStockList(): List<StockEntity>

    @Query("SELECT * FROM dividends")
    fun getDividends(): Flow<List<DividendEntity>>

    @Transaction
    @Query("SELECT * FROM stocks")
    fun getStockWithDividends(): Flow<List<StockWithDividendEntity>>



    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStock(symbol: String): StockEntity?

    @Transaction
    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStockWithDividend(symbol: String): StockWithDividendEntity?


    @Insert
    suspend fun insertStock(stock: StockEntity)

    @Update
    suspend fun updateStock(stock: StockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDividend(dividend: DividendEntity)


    @Query("DELETE FROM stocks WHERE symbol = :symbol")
    suspend fun deleteStock(symbol: String)

    @Query("DELETE FROM stocks")
    suspend fun clearStocks()

    @Query("DELETE FROM dividends WHERE parentSymbol = :symbol")
    suspend fun deleteDividends(symbol: String)

    @Transaction
    suspend fun deleteStockWithDividends(symbol: String) {
        deleteStock(symbol)
        deleteDividends(symbol)
    }
}
