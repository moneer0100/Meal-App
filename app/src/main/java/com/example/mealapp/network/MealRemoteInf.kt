package com.example.mealapp.network

import com.example.mealapp.model.CategoryResponse
import com.example.mealapp.model.RandomResponse
import com.example.mealapp.model.SubCategoryMeal
import com.example.mealapp.model.SubCategoryResponse

interface MealRemoteInf {
    suspend fun getRandom():RandomResponse
    suspend fun getCategory():CategoryResponse
    suspend fun getSubCategory(category:String): SubCategoryResponse
}