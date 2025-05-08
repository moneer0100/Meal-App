package com.example.mealapp.model.dataBase

import com.example.mealapp.model.pojo.MealDataFav
import kotlinx.coroutines.flow.Flow

interface MealLocalInterface {
    fun getAllData(): Flow<List<MealDataFav>>
    suspend fun insertFav(meal: MealDataFav)
    suspend fun deleteFav(meal: MealDataFav)
}