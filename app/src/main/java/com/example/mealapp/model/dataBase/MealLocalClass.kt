package com.example.mealapp.model.dataBase

import com.example.mealapp.model.pojo.MealDataFav
import kotlinx.coroutines.flow.Flow

class MealLocalClass(private val dao: Dao):MealLocalInterface {
    companion object {
        @Volatile
        var instance: MealLocalClass? = null
        fun getInstance(dao: Dao): MealLocalClass {
            return instance?: synchronized(this){
                instance?: MealLocalClass(dao)
                    .also { instance = it }
            }
        }
    }

    override fun getAllData(): Flow<List<MealDataFav>> {
        return dao.getAllData()
    }

    override suspend fun insertFav(meal: MealDataFav) {
      return dao.insertFav(meal)
    }

    override suspend fun deleteFav(meal: MealDataFav) {
       return dao.deleteFav(meal)
    }
}