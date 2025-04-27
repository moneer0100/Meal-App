package com.example.mealapp.network

import com.example.mealapp.model.RandomResponse

interface MealRemoteInf {
    suspend fun getRandom():RandomResponse
}