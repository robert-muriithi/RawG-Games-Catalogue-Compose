package dev.robert.shared

data class ApiResponse(
    val resultDtos: Any
) {
    val isSuccessful: Boolean
        get() = true
}