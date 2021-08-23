package com.mobiqity.weather.api

sealed class APIResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : APIResult<T>()
    data class Failure<out T : Any>(val data: T) : APIResult<T>()
    data class Error(val exception: Exception, val data: Nothing?, val message: String) : APIResult<Nothing>()
    data class Loading(val isLoadig: Boolean) : APIResult<Nothing>()
}