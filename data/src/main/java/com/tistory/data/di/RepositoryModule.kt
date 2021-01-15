package com.tistory.data.di

import com.tistory.data.repository.StockWithDividendRepositoryImpl
import com.tistory.data.source.local.StockDao
import com.tistory.data.source.remote.api.StockApi
import com.tistory.domain.repository.StockWithDividendRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    fun provideStockWithDividendRepo(
        stockDao: StockDao,
        stockApi: StockApi
    ): StockWithDividendRepository {
        return StockWithDividendRepositoryImpl(
            stockDao, stockApi
        )
    }
}