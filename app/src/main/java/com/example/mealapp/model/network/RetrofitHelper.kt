package com.example.mealapp.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val url =  "https://www.themealdb.com/api/json/v1/1/"

    val retrofitinstanceCurrent: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: ApiService by lazy {
        retrofitinstanceCurrent.create(ApiService::class.java)
    }
}