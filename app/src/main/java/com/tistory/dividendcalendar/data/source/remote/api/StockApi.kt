package com.tistory.dividendcalendar.data.source.remote.api

import com.tistory.dividendcalendar.data.source.remote.model.DividendResponse
import com.tistory.dividendcalendar.data.source.remote.model.LogoResponse
import com.tistory.dividendcalendar.data.source.remote.model.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApi {

    @GET("{symbol}/logo")
    suspend fun getLogo(
        @Path("symbol") symbol: String,
        @Query("token") token: String
    ): LogoResponse

    @GET("{symbol}/company")
    suspend fun getProfile(
        @Path("symbol") symbol: String,
        @Query("token") token: String
    ): ProfileResponse

    @GET("{symbol}/dividends/{range}")
    suspend fun getDividend(
        @Path("symbol") symbol: String,
        @Path("range") range: String,
        @Query("token") token: String
    ): Any //DividendResponse

    @GET("{symbol}/dividends/{range}")
    suspend fun getDividends(
        @Path("symbol") symbol: String,
        @Path("range") range: String,
        @Query("token") token: String
    ): List<DividendResponse>
}