package com.example.mealapp.model

import com.google.gson.annotations.SerializedName


data class RandomResponse(
    @SerializedName("meals")
    val dataRandomMeal: List<RandomMeal>)