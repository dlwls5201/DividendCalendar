package com.tistory.dividendcalendar.di

import android.content.Context
import com.tistory.data.repository.StockRepositoryImpl
import com.tistory.data.source.local.StockDataBase
import com.tistory.data.source.remote.ApiProvider
import com.tistory.domain.repository.StockRepository
import com.tistory.domain.usecase.StockUsecase

object Injection {

    fun provideStockUsecase(context: Context): StockUsecase {
        return StockUsecase(
            provideStockRepository(context)
        )
    }

    fun provideStockRepository(context: Context): StockRepository {
        return StockRepositoryImpl(
            StockDataBase.getInstance(context).getStockDao(),
            ApiProvider.provideStockApi()
        )
    }
}