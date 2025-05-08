package com.example.mealapp.model.network

sealed class ResponseState<out T> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class Error(val message: Throwable) : ResponseState<Nothing>()
    data object Loading : ResponseState<Nothing>()
}