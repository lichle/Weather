package com.lichle.weather.domain

sealed class Response<out T> {
    data class Success<out T>(val data: T): Response<T>()
    data class Error(val code: Int, val message: String = "Unknown Error"): Response<Nothing>()
    data object Loading : Response<Nothing>()
}