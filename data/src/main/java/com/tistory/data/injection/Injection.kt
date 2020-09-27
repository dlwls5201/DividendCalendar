package com.tistory.data.injection

import com.tistory.data.repository.StockRepository
import com.tistory.data.repository.StockRepositoryImpl
import com.tistory.data.source.local.StockDataBase
import com.tistory.data.source.remote.ApiProvider

object Injection {

    fun provideStockRepository(): StockRepository {
        return StockRepositoryImpl(
            StockDataBase.getInstance().getStockDao(),
            ApiProvider.provideInvitationApi()
        )
    }
}
