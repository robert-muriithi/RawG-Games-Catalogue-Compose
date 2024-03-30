package dev.robert.search.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.search.domain.model.Game
import dev.robert.search.domain.usecase.AddToRecentSearchUseCase
import dev.robert.search.domain.usecase.GetRecentSearchesUseCase
import dev.robert.search.domain.usecase.SearchUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val addToRecentSearchUseCase: AddToRecentSearchUseCase,
    private val getRecentSearchUseCase: GetRecentSearchesUseCase,
) : ViewModel() {


    private val _query = mutableStateOf("")
    val query = _query as State<String>

    private val _gamesState = mutableStateOf(StateHolder<Flow<PagingData<Game>>>())
    val gamesState: State<StateHolder<Flow<PagingData<Game>>>> = _gamesState

    private val _recentSearches = mutableStateOf(StateHolder<Flow<List<Game>>>())
    val recentSearches: State<StateHolder<Flow<List<Game>>>> = _recentSearches

    init {
        getRecentSearches()
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    fun clearSearchResults() {
        _gamesState.value = _gamesState.value.copy(data = null)
    }

    fun search() {
        viewModelScope.launch {
            _gamesState.value = _gamesState.value.copy(loading = true)
            val result = searchUseCase(query.value).cachedIn(viewModelScope)
            _gamesState.value = _gamesState.value.copy(data = result, loading = false)
        }
    }

    fun addToRecentSearch(id: Int, recentSearch: Boolean) {
        viewModelScope.launch {
            addToRecentSearchUseCase(id, recentSearch)
        }
    }


    private fun getRecentSearches() {
        _recentSearches.value = _recentSearches.value.copy(loading = true)
        viewModelScope.launch {
            val result = getRecentSearchUseCase()
            _recentSearches.value = _recentSearches.value.copy(data = result, loading = false)
        }
    }


    data class StateHolder<T>(
        val data: T? = null,
        val error: String? = null,
        val loading: Boolean = false
    )
}