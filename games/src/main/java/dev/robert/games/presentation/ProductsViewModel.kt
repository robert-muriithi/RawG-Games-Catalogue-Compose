package dev.robert.games.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.products.domain.model.Product
import dev.robert.games.domain.usecase.GetGenresUseCase
import dev.robert.games.domain.usecase.GetProductById
import dev.robert.games.domain.usecase.GetProductCategory
import dev.robert.games.domain.usecase.GetGamesUseCase
import dev.robert.products.presentation.StateHolder
import dev.robert.shared.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getProductById: GetProductById,
    private val getProductCategory: GetProductCategory,
    private val getGenresUseCase: GetGenresUseCase,
) : ViewModel() {

    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory
    fun setCategory(value: String) {
        _selectedCategory.value = value
    }

    private val _productCategoryState = mutableStateOf(StateHolder<Product>())
    val productCategoryState: State<StateHolder<Product>> = _productCategoryState

    fun getProductCategory(category: String) {
        viewModelScope.launch {
            getProductCategory.invoke(category).collectLatest {
                when (it) {
                    is dev.robert.shared.utils.Resource.Success -> {
                        it.value.let { product ->
                            _productsState.value = productsState.value.copy(
                                isLoading = false,
                                data = product
                            )
                        }
                    }

                    is dev.robert.shared.utils.Resource.Failure -> {
                        _productsState.value = productsState.value.copy(
                            isLoading = false,
                            error = it.throwable.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            getProductById.invoke(id).collectLatest {
                when (it) {
                    is dev.robert.shared.utils.Resource.Success -> {
                        it.value.let { product ->
                            _productCategoryState.value = productCategoryState.value.copy(
                                isLoading = false,
                                data = product
                            )
                        }
                    }

                    is dev.robert.shared.utils.Resource.Failure -> {
                        _productCategoryState.value = productCategoryState.value.copy(
                            isLoading = false,
                            error = it.throwable.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    private val _categoriesState = mutableStateOf(StateHolder<List<String>>())
    val categoriesState: State<StateHolder<List<String>>> = _categoriesState

    private val _productsState = mutableStateOf(StateHolder<List<Product>>())
    val productsState: State<StateHolder<List<Product>>> = _productsState

    private val _products = mutableStateOf(StateHolder<List<Product>>())
    val products: State<StateHolder<List<Product>>> = _products

    private fun getProductCategories() {
        _categoriesState.value = categoriesState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            getGenresUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.value.let { categories ->
                            _categoriesState.value = categoriesState.value.copy(
                                isLoading = false,
                                data = listOf("All") + categories
                            )
                        }
                    }

                    is Resource.Failure -> {
                        _categoriesState.value = categoriesState.value.copy(
                            isLoading = false,
                            error = it.throwable.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    private fun getProducts() {
        _products.value = products.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            getGamesUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.value.let { data ->
                            _products.value = products.value.copy(
                                isLoading = false,
                                data = data
                            )
                        }
                    }

                    is Resource.Failure -> {
                        _products.value = products.value.copy(
                            isLoading = false,
                            error = it.throwable.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val categories = async { getProductCategories() }
            categories.await()
            getProducts()
        }
    }

}