package com.tistory.data.source.remote

import com.tistory.data.BuildConfig
import com.tistory.data.source.remote.api.StockApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider {

    //private const val baseUrl = "https://cloud.iexapis.com/stable/stock/"
    private const val baseUrl = "https://cloud.iexapis.com/"

    const val token = ApiKey.iex_cloud_api_key

    fun provideStockApi(): StockApi = getRetrofitBuild()
        .create(StockApi::class.java)

    private fun getRetrofitBuild() = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getOkhttpClient())
        .addConverterFactory(getGsonConverter())
        .build()

    private fun getGsonConverter() = GsonConverterFactory.create()

    private fun getOkhttpClient() = OkHttpClient.Builder().apply {

        //TimeOut 시간을 지정합니다.
        readTimeout(60, TimeUnit.SECONDS)
        connectTimeout(60, TimeUnit.SECONDS)
        writeTimeout(5, TimeUnit.SECONDS)

        // 이 클라이언트를 통해 오고 가는 네트워크 요청/응답을 로그로 표시하도록 합니다.
        addInterceptor(getLoggingInterceptor())
    }.build()

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
}