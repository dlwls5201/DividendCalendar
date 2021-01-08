package com.tistory.data.source.remote.api

import com.tistory.data.di.ApiModule
import com.tistory.data.source.remote.model.DividendResponse
import com.tistory.data.source.remote.model.LogoResponse
import com.tistory.data.source.remote.model.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApi {

    @GET("stable/stock/{symbol}/dividends/{range}")
    suspend fun getDividend(
        @Path("symbol") symbol: String,
        @Path("range") range: String = "next",
        @Query("token") token: String = ApiModule.token
    ): Any

    @GET("stable/stock/{symbol}/dividends/{range}")
    suspend fun getDividends(
        @Path("symbol") symbol: String,
        @Path("range") range: String,
        @Query("token") token: String = ApiModule.token
    ): List<DividendResponse>

    @GET("stable/stock/{symbol}/logo")
    suspend fun getLogo(
        @Path("symbol") symbol: String,
        @Query("token") token: String = ApiModule.token
    ): LogoResponse

    @GET("stable/stock/{symbol}/company")
    suspend fun getProfile(
        @Path("symbol") symbol: String,
        @Query("token") token: String = ApiModule.token
    ): ProfileResponse
}