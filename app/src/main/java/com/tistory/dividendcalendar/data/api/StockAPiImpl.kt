package com.tistory.dividendcalendar.data.api

import com.tistory.dividendcalendar.presentation.main.model.DividendsApiModel
import com.tistory.dividendcalendar.presentation.main.model.LogoApiModel
import com.tistory.dividendcalendar.presentation.main.model.StockApiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockAPiImpl {
    @GET("stable/stock/{ticker}/company/batch")
    fun companyInfo(
        @Path("ticker") ticker: String,
        @Query("token") token: String
    ): Call<StockApiModel>

    @GET("stable/stock/{ticker}/logo/batch")
    fun companyLogo(
        @Path("ticker") ticker: String,
        @Query("token") token: String
    ): Call<LogoApiModel>

    @GET("stable/stock/{ticker}/dividends/{range}")
    fun companyDividends(
        @Path("ticker") ticker: String,
        @Path("range") range: String,
        @Query("token") token: String
    ): Call<DividendsApiModel>
}