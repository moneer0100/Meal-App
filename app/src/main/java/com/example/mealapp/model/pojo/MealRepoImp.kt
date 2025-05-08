package com.example.mealapp.model.pojo

import com.example.mealapp.model.dataBase.MealLocalClass
import com.example.mealapp.model.dataBase.MealLocalInterface
import com.example.mealapp.model.network.MealRemoteImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MealRepoImp(private val mealRepo: MealRemoteImp,private val mealLocal:MealLocalClass): MealRepoInf {
    companion object {
        private var instance: MealRepoImp? = null
        fun getInstance(remoteSource: MealRemoteImp,mealLocal: MealLocalClass): MealRepoImp {
            return instance ?: synchronized(this) {
                instance ?: MealRepoImp(remoteSource,mealLocal)
                    .also { instance = it }
            }
        }


    }

    override suspend fun getRandom(): Flow<List<RandomMeal>> {
        val response =mealRepo.getRandom()

        return flowOf(response.dataRandomMeal)

    }

    override suspend fun getCategory(): Flow<List<CategoryMeal>> {
        val response=mealRepo.getCategory()
        return flowOf(response.categories)
    }

    override suspend fun getSubCategory(category: String): Flow<List<SubCategoryMeal>> {
        val response=mealRepo.getSubCategory(category)
        return flowOf(response.meals)
    }

    override suspend fun getCategoryDetails(id: String): Flow<List<Meals>> {
        val response=mealRepo.getCategoryDetails(id)
        return flowOf(response.meals)
    }

    override fun getAllMeal(): Flow<List<MealDataFav>> {
        return mealLocal.getAllData()
    }

    override suspend fun insertMovieToDav(meal: MealDataFav) {
       return mealLocal.insertFav(meal)
    }

    override suspend fun deleteMovieFromFav(meal: MealDataFav) {
       return mealLocal.deleteFav(meal)
    }

}