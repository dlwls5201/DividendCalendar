package com.tistory.dividendcalendar.data.source.local

import androidx.room.*
import com.tistory.dividendcalendar.data.source.local.entity.DividendEntity
import com.tistory.dividendcalendar.data.source.local.entity.LogoEntity
import com.tistory.dividendcalendar.data.source.local.entity.SymbolEntity
import com.tistory.dividendcalendar.data.source.local.entity.SymbolWithDividends

@Dao
interface StockDao {

    //logo
    @Query("SELECT * FROM logos WHERE symbol = :symbol")
    suspend fun getLogo(symbol: String): LogoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogo(logo: LogoEntity)

    @Query("DELETE FROM logos WHERE symbol = :symbol")
    suspend fun deleteLogoBySymbol(symbol: String)

    @Query("DELETE FROM logos")
    suspend fun clearLogo()


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