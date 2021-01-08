package com.tistory.data.di

import android.content.Context
import com.tistory.data.source.local.StockDao
import com.tistory.data.source.local.StockDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideDao(
        stockDataBase: StockDataBase
    ): StockDao = stockDataBase.getStockDao()

    @Provides
    fun provideStockDataDatabase(
        @ApplicationContext context: Context
    ): StockDataBase = StockDataBase.getInstance(context)
}