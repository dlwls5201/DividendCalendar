package com.tistory.data.source.remote.api

import com.tistory.data.source.remote.ApiProvider
import com.tistory.data.source.remote.model.DividendResponse
import com.tistory.data.source.remote.model.LogoResponse
import com.tistory.data.source.remote.model.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApi {

    /*@GET("stable/time-series/advanced_dividends/{symbol}")
    suspend fun getDividends(
        @Path("symbol") symbol: String,
        @Query("last") last: String = "1",
        @Query("token") token: String = ApiProvider.token
    ): List<DividendResponse>*/

    @GET("stable/stock/{symbol}/dividends/{range}")
    suspend fun getDividend(
        @Path("symbol") symbol: String,
        @Path("range") range: String = "next",
        @Query("token") token: String = ApiProvider.token
    ): Any

    @GET("stable/stock/{symbol}/dividends/{range}")
    suspend fun getDividends(
        @Path("symbol") symbol: String,
        @Path("range") range: String,
        @Query("token") token: String = ApiProvider.token
    ): List<DividendResponse>

    @GET("stable/stock/{symbol}/logo")
    suspend fun getLogo(
        @Path("symbol") symbol: String,
        @Query("token") token: String = ApiProvider.token
    ): LogoResponse

    @GET("stable/stock/{symbol}/company")
    suspend fun getProfile(
        @Path("symbol") symbol: String,
        @Query("token") token: String = ApiProvider.token
    ): ProfileResponse
}