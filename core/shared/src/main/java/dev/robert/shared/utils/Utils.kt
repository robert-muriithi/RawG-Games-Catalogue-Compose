package dev.robert.shared.utils

// safeApiCall is a function that takes a suspend function as a parameter and returns a TagsResultDto
suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        Resource.Failure(throwable)
    }
}

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(val throwable: Throwable) : Resource<Nothing>()
}