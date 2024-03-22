package dev.robert.games.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.games.domain.usecase.GetGenresUseCase
import dev.robert.games.domain.usecase.GetGamesUseCase
import dev.robert.games.domain.usecase.GetHotGamesUseCase
import dev.robert.games.presentation.events.HomeScreenEvent
import dev.robert.navigation.navigation.Destinations
import dev.robert.network.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private val event = mutableStateOf<HomeScreenEvent?>(null)
    val currentEvent: State<HomeScreenEvent?> = event

    fun setCategory(value: String) {
        _selectedCategory.value = value
    }

    private lateinit var navController: NavController

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun onEvent(event: HomeScreenEvent) {
        when(event) {
            is HomeScreenEvent.NavigateToGameDetails -> {
                navController.navigate(Destinations.GameDetailsScreen.route + "/${event.id}")
            }
            is HomeScreenEvent.NavigateToSearch -> {
                navController.navigate(Destinations.SearchScreen.route)
            }
            is HomeScreenEvent.NavigateToBookmarks -> {
                navController.navigate(Destinations.BookMarksScreen.route)
            }

            is HomeScreenEvent.NavigateToCategory -> {
//                navController.navigate(Destinations.CategoryScreen.route)
            }
        }
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

    fun getHotGames(refresh : Boolean) {
        _hotGamesState.value = hotGamesState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            getHotGamesUseCase.invoke(refresh).collectLatest {
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

    fun getGames(searchTerm: String? = null) {
        _gamesState.value = gamesState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
         val games = getGamesUseCase.invoke(query = searchTerm).cachedIn(viewModelScope)
            _gamesState.value = gamesState.value.copy(
                isLoading = false,
                data = games
            )
        }
    }


    init {
        viewModelScope.launch {
            val hotGames = async { getHotGames(false) }
            hotGames.await()
            val categories = async { getProductCategories() }
            categories.await()
            val games = async { getGames() }
            games.await()
        }
    }

}