package dev.robert.search.domain.repository

import androidx.paging.PagingData
import dev.robert.search.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(query: String) : Flow<PagingData<Game>>

    fun getRecentSearches() : Flow<List<Game>>
    suspend fun updateRecentSearch(id: Int, recentSearch: Boolean)
}