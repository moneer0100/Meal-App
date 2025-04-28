package com.example.mealapp.network

import com.example.mealapp.model.CategoryResponse
import com.example.mealapp.model.RandomResponse

interface MealRemoteInf {
    suspend fun getRandom():RandomResponse
    suspend fun getCategory():CategoryResponse
}