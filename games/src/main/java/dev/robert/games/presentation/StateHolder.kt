package dev.robert.games.presentation


data class StateHolder<T>(
    val error: String? = null,
    val isLoading: Boolean = false,
    val data: T? = null
)