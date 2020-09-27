package com.tistory.dividendcalendar.injection

import android.content.Context
import com.tistory.data.source.local.StockDataBase
import com.tistory.data.source.remote.ApiProvider
import com.tistory.dividendcalendar.repository.StockRepository
import com.tistory.dividendcalendar.repository.StockRepositoryImpl

object Injection {

    fun provideStockRepository(context: Context): StockRepository {
        return StockRepositoryImpl(
            StockDataBase.getInstance(context).getStockDao(),
            ApiProvider.provideInvitationApi()
        )
    }
}
