package com.example.mealapp.network


import com.example.mealapp.model.CategoryResponse
import com.example.mealapp.model.RandomResponse

class MealRemoteImp private constructor(private val apiService: ApiService):MealRemoteInf {
    companion object {
        private var instance: MealRemoteImp? = null
        fun getInstance(apiService: ApiService): MealRemoteImp {
            return instance ?: synchronized(this) {
                instance ?: MealRemoteImp(apiService).also { instance = it }
            }
        }
    }

    override suspend fun getRandom(): RandomResponse {

        return apiService.getRandom()
    }

    override suspend fun getCategory(): CategoryResponse {
        return apiService.getCategory()
    }

}