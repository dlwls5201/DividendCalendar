package com.tistory.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tistory.data.source.local.entity.StockNameEntity

@Dao
interface StockNameDao {

    @Query("SELECT * FROM ticker WHERE companyName LIKE :query OR ticker LIKE :query")
    suspend fun getTickers(query: String): List<StockNameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicker(ticker: StockNameEntity)

    @Query("DELETE FROM ticker")
    suspend fun clearTickers()
}