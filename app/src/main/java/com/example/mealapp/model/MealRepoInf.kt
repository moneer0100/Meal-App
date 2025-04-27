package com.example.mealapp.model

import kotlinx.coroutines.flow.Flow

interface MealRepoInf {
    suspend fun getRandom():Flow<List<RandomMeal>>
}