package com.tistory.domain.di

import com.tistory.domain.repository.ModifyStockCountUsecase
import com.tistory.domain.repository.StockWithDividendRepository
import com.tistory.domain.usecase.AddStockUsecase
import com.tistory.domain.usecase.DeleteStockUsecase
import com.tistory.domain.usecase.GetStockItemsUsecase
import com.tistory.domain.usecase.RefreshAllStockDividendUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object UsecaseModule {

    @Provides
    fun provideAddStockUsecase(stockWithDividendRepository: StockWithDividendRepository) =
        AddStockUsecase(stockWithDividendRepository)

    @Provides
    fun provideRefreshAllStockDividendUsecase(stockWithDividendRepository: StockWithDividendRepository) =
        RefreshAllStockDividendUsecase(stockWithDividendRepository)

    @Provides
    fun provideGetStockItemsUsecase(stockWithDividendRepository: StockWithDividendRepository) =
        GetStockItemsUsecase(stockWithDividendRepository)

    @Provides
    fun provideDeleteStockUsecase(stockWithDividendRepository: StockWithDividendRepository) =
        DeleteStockUsecase(stockWithDividendRepository)

    @Provides
    fun provideModifyStockCountUsecase(stockWithDividendRepository: StockWithDividendRepository) =
        ModifyStockCountUsecase(stockWithDividendRepository)
}