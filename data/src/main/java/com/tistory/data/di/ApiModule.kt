package com.tistory.data.di

import com.tistory.data.source.remote.ApiKey
import com.tistory.data.source.remote.api.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
object ApiModule {

    const val token = ApiKey.iex_cloud_api_key

    @Provides
    fun provideStockApi(retrofit: Retrofit) = retrofit.create(StockApi::class.java)
}