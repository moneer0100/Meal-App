package com.example.mealapp.network

import com.example.mealapp.model.CategoryResponse
import com.example.mealapp.model.RandomResponse
import com.example.mealapp.model.SubCategoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    suspend fun getRandom(): RandomResponse
    @GET("categories.php")
    suspend fun getCategory():CategoryResponse
    @GET("filter.php")
    suspend fun getSubCategory(@Query("c") category :String):SubCategoryResponse


}