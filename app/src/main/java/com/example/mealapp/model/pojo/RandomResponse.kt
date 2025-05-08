package com.example.mealapp.model.pojo

import com.google.gson.annotations.SerializedName


data class RandomResponse(
    @SerializedName("meals")
    val dataRandomMeal: List<RandomMeal>)