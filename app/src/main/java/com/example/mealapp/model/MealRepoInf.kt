package com.example.mealapp.model

import kotlinx.coroutines.flow.Flow

interface MealRepoInf {
    suspend fun getRandom():Flow<List<RandomMeal>>
    suspend fun getCategory():Flow<List<CategoryMeal>>
    suspend fun getSubCategory(category:String):Flow<List<SubCategoryMeal>>
}