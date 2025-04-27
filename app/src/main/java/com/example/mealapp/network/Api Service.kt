package com.example.mealapp.network

import com.example.mealapp.model.RandomResponse
import retrofit2.http.GET

interface ApiService {
    @GET("random.php")
    suspend fun getRandom(): RandomResponse


}