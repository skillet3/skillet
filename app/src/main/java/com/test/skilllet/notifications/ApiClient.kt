package com.test.skilllet.notifications

import com.test.skilllet.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {
    val apiService: APIService
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.FCM_BASE_URL)
            .client(provideClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)

    private fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { chain ->
            val request: Request = chain.request()
            chain.proceed(request)
        }.build()
    }
}