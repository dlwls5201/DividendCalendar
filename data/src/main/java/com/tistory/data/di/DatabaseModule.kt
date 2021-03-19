package com.tistory.data.di

import android.content.Context
import com.tistory.data.source.local.StockDao
import com.tistory.data.source.local.StockDataBase
import com.tistory.data.source.local.StockNameDao
import com.tistory.data.source.local.StockNameDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideStockDao(
        stockDataBase: StockDataBase
    ): StockDao = stockDataBase.getStockDao()

    @Provides
    fun provideStockDataDatabase(
        @ApplicationContext context: Context
    ): StockDataBase = StockDataBase.getInstance(context)

    @Provides
    fun provideStockNameDao(
        stockNameDataBase: StockNameDataBase
    ): StockNameDao = stockNameDataBase.getTickerDao()

    @Provides
    fun provideStockNameDataDatabase(
        @ApplicationContext context: Context
    ): StockNameDataBase = StockNameDataBase.getInstance(context)
}