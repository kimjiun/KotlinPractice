package com.kimjiun.ch18_network

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyRetrofit: Application() {
    val networkService: RetrofitService

    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://www.daum.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        networkService = retrofit.create(RetrofitService::class.java)
    }
}