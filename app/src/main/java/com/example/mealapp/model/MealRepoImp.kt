package com.example.mealapp.model

import android.util.Log
import com.example.mealapp.network.MealRemoteImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MealRepoImp(private val mealRepo: MealRemoteImp):MealRepoInf {
    companion object {
        private var instance: MealRepoImp? = null
        fun getInstance(remoteSource: MealRemoteImp): MealRepoImp {
            return instance ?: synchronized(this) {
                instance?: MealRepoImp(remoteSource)
                    .also { instance = it }
            }
        }


    }

    override suspend fun getRandom(): Flow<List<RandomMeal>> {
        val response =mealRepo.getRandom()

        return flowOf(response.dataRandomMeal)

    }
}