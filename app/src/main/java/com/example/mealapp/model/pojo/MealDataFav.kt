package com.example.mealapp.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favMeal")
data class MealDataFav(
    @PrimaryKey(autoGenerate = true)
    val id :Long,
    val img :String?=null,
    val title:String?=null,
    val media:String?=null,
    var isFavorite: Boolean = false
)