package com.example.mealapp.model.network

import com.example.mealapp.model.pojo.CategoryResponse
import com.example.mealapp.model.pojo.MealDetailsResponse
import com.example.mealapp.model.pojo.RandomResponse
import com.example.mealapp.model.pojo.SubCategoryResponse

interface MealRemoteInf {
    suspend fun getRandom(): RandomResponse
    suspend fun getCategory(): CategoryResponse
    suspend fun getSubCategory(category:String): SubCategoryResponse
    suspend fun getCategoryDetails(id:String): MealDetailsResponse
}