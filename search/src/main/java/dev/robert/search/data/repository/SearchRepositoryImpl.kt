package dev.robert.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.robert.database.database.GamesDatabase
import dev.robert.network.apiservice.GamesApi
import dev.robert.search.data.mappers.toGame
import dev.robert.search.data.pager.SearchPager
import dev.robert.search.domain.model.Game
import dev.robert.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl(
    private val gamesApi: GamesApi,
    private val appDb: GamesDatabase,
) : SearchRepository {


    override fun search(query: String):  Flow<PagingData<Game>> {
        val flow = Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = {
                SearchPager(
                    api = gamesApi,
                    searchString = query,
                    searchExact = false
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toGame() }
        }
        return flow
    }

    override fun getRecentSearches(): Flow<List<Game>> = flow {
        appDb.gameEntityDao().getRecentSearches().map { it.map { it.toGame() } }
    }

    override suspend fun updateRecentSearch(id: Int, recentSearch: Boolean) {
        appDb.gameEntityDao().updateRecentSearch(id, recentSearch)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}