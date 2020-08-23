package com.tistory.dividendcalendar.data.injection

import com.tistory.dividendcalendar.data.repository.StockRepository
import com.tistory.dividendcalendar.data.repository.StockRepositoryImpl
import com.tistory.dividendcalendar.data.source.local.StockDataBase
import com.tistory.dividendcalendar.data.source.remote.ApiProvider


object Injection {

    fun provideInvitationRepository(): StockRepository {
        return StockRepositoryImpl(
            StockDataBase.getInstance().getStockDao(),
            ApiProvider.provideInvitationApi()
        )
    }
}