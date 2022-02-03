package com.androidcourse.leaguewiki.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(baseUrl: String) {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient
            .Builder()
            .build()
    }

    private val gson: Gson = GsonBuilder().setLenient().create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

}