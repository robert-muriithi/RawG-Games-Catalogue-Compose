package dev.robert.products.presentation

import dev.robert.products.domain.model.Product

/*data class ProductState (
    val error: String? = null,
    val isLoading: Boolean = false,
    val
)*/
data class StateHolder<T>(
    val error: String? = null,
    val isLoading: Boolean = false,
    val data: T? = null
)