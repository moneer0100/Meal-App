package com.example.mealapp.model.pojo

data class CategoryMeal(

    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String,
    var isFavorite: Boolean = false
)