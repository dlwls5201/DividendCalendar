package com.tistory.dividendcalendar.data.source.local

import androidx.room.*
import com.tistory.dividendcalendar.data.source.local.entity.DividendEntity
import com.tistory.dividendcalendar.data.source.local.entity.ProfileEntity
import com.tistory.dividendcalendar.data.source.local.entity.SymbolEntity
import com.tistory.dividendcalendar.data.source.local.entity.SymbolWithDividends

@Dao
interface StockDao {

    //profile
    @Query("SELECT * FROM profiles WHERE symbol = :symbol")
    suspend fun getProfile(symbol: String): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("DELETE FROM profiles WHERE symbol = :symbol")
    suspend fun deleteProfileBySymbol(symbol: String)

    @Query("DELETE FROM profiles")
    suspend fun clearProfile()

    //dividend
    @Transaction
    @Query("SELECT * FROM symbols")
    suspend fun getSymbolWithDividends(): List<SymbolWithDividends>

    @Query("SELECT * FROM symbols WHERE symbol = :symbol")
    suspend fun getSymbol(symbol: String): SymbolEntity?

    @Query("SELECT * FROM dividends WHERE parentSymbol = :symbol")
    suspend fun getDividends(symbol: String): List<DividendEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbol(symbol: SymbolEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDividend(dividend: DividendEntity)
}