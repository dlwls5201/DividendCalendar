package com.tistory.dividendcalendar.di

import android.content.Context
import com.tistory.data.repository.StockWithDividendRepositoryImpl
import com.tistory.data.source.local.StockDataBase
import com.tistory.data.source.remote.ApiProvider
import com.tistory.domain.repository.StockWithDividendRepository
import com.tistory.domain.usecase.AddStockUsecase
import com.tistory.domain.usecase.RefreshAllStockDividendUsecase

object Injection {

    private lateinit var stockDao: StockDataBase

    fun init(context: Context) {
        stockDao = StockDataBase.getInstance(context)
    }

    fun provideAddStockUsecase() = AddStockUsecase(provideStockWithDividendRepo())

    fun provideRefreshAllStockDividendUsecase() =
        RefreshAllStockDividendUsecase(provideStockWithDividendRepo())

    fun provideStockWithDividendRepo(): StockWithDividendRepository {
        return StockWithDividendRepositoryImpl(
            stockDao.getStockDao(),
            ApiProvider.provideStockApi()
        )
    }
}