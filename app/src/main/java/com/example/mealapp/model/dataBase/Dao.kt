package com.example.mealapp.model.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mealapp.model.pojo.MealDataFav
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    @Query("SELECT * FROM favMeal")
    fun getAllData(): Flow<List<MealDataFav>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(movie: MealDataFav)

    @Delete
    suspend fun deleteFav(movie: MealDataFav)
}