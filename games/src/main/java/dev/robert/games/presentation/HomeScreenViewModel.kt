package dev.robert.games.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.products.domain.model.Product
import dev.robert.games.domain.usecase.GetGenresUseCase
import dev.robert.games.domain.usecase.GetGamesUseCase
import dev.robert.games.domain.usecase.GetHotGamesUseCase
import dev.robert.shared.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val getHotGamesUseCase: GetHotGamesUseCase
) : ViewModel() {

    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory
    fun setCategory(value: String) {
        _selectedCategory.value = value
    }

    fun onGameSelected(game: GamesResultModel) {
    }

    /*fun onProductCategorySelected(product: Product) {
        _productCategoryState.value = productCategoryState.value.copy(
            data = product
        )
    }*/

    /*fun onProductSelected(game: GamesResultModel) {
        _gamesState.value = gamesState.value.copy(
            data = game
        )
    }*/

    /*private val _productCategoryState = mutableStateOf(StateHolder<Product>())
    val productCategoryState: State<StateHolder<Product>> = _productCategoryState*/


    private val _genresState = mutableStateOf(StateHolder<Flow<PagingData<Genre>>>())
    val genresState: State<StateHolder<Flow<PagingData<Genre>>>> = _genresState

    /*private val _productsState = mutableStateOf(StateHolder<List<Product>>())
    val productsState: State<StateHolder<List<Product>>> = _productsState*/

    /*private val _products = mutableStateOf(StateHolder<List<Product>>())
    val products: State<StateHolder<List<Product>>> = _products*/

    private val _gamesState = mutableStateOf(StateHolder<Flow<PagingData<GamesResultModel>>>())
    val gamesState: State<StateHolder<Flow<PagingData<GamesResultModel>>>> = _gamesState


    private val _hotGamesState = mutableStateOf(StateHolder<List<GamesResultModel>>())
    val hotGamesState: State<StateHolder<List<GamesResultModel>>> = _hotGamesState

    private fun getProductCategories()  {
        _genresState.value = genresState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val genres = getGenresUseCase.invoke().cachedIn(viewModelScope)
            _genresState.value = genresState.value.copy(
                isLoading = false,
                data = genres
            )
        }
    }

    private fun getHotGames() {
        _hotGamesState.value = hotGamesState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            getHotGamesUseCase().collectLatest {
                when(it) {
                    is Resource.Failure -> {
                        _hotGamesState.value = hotGamesState.value.copy(
                            isLoading = false,
                            error = it.throwable.message ?: "Unknown error"
                        )
                    }
                    is Resource.Success -> {
                        _hotGamesState.value = hotGamesState.value.copy(
                            isLoading = false,
                            data = it.value
                        )
                    }
                }
            }
            }
    }

    private fun getGames() {
        _gamesState.value = gamesState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
         val games = getGamesUseCase.invoke().cachedIn(viewModelScope)
            _gamesState.value = gamesState.value.copy(
                isLoading = false,
                data = games
            )
        }
    }


    init {
        viewModelScope.launch {
            val hotGames = async { getHotGames() }
            hotGames.await()
            val categories = async { getProductCategories() }
            categories.await()
            val games = async { getGames() }
            games.await()
        }
    }

}