package com.example.mealapp.model.network

import com.example.mealapp.model.pojo.CategoryResponse
import com.example.mealapp.model.pojo.MealDetailsResponse
import com.example.mealapp.model.pojo.RandomResponse
import com.example.mealapp.model.pojo.SubCategoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    suspend fun getRandom(): RandomResponse
    @GET("categories.php")
    suspend fun getCategory(): CategoryResponse
    @GET("filter.php")
    suspend fun getSubCategory(@Query("c") category :String): SubCategoryResponse
    @GET("lookup.php")
    suspend fun getCategoryDetails(@Query("i") id:String): MealDetailsResponse


}