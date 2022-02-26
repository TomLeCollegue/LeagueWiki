package com.androidcourse.leaguewiki.model

sealed class DataResult<out T> {

    data class Loading<out T>(val partialData: T? = null, val progress: Float? = null) : DataResult<T>()
    data class Success<out T>(val successData: T) : DataResult<T>()
    data class Failure<out T>(val throwable: Throwable? = null, val failureData: T? = null) : DataResult<T>()

    val data: T?
        get() {
            return when (this) {
                is Loading -> partialData
                is Success -> successData
                is Failure -> failureData
            }
        }
}
