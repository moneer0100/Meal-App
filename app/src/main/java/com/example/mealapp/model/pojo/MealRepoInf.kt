package com.example.mealapp.model.pojo

import kotlinx.coroutines.flow.Flow

interface MealRepoInf {
    suspend fun getRandom():Flow<List<RandomMeal>>
    suspend fun getCategory():Flow<List<CategoryMeal>>
    suspend fun getSubCategory(category:String):Flow<List<SubCategoryMeal>>
    suspend fun getCategoryDetails(id:String):Flow<List<Meals>>

    //Fav
fun getAllMeal():Flow<List<MealDataFav>>
    suspend fun insertMovieToDav(movieData: MealDataFav)
    suspend fun deleteMovieFromFav(movieData: MealDataFav)
}