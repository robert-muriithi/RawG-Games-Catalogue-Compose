package dev.robert.games.domain.repository

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.GameGenre
import dev.robert.games.domain.model.genre.Genre
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    fun getGameGenres() : Flow<PagingData<Genre>>

    fun getGames() : Flow<PagingData<GamesResultModel>>

    fun searchGames(query: String, searchExact: Boolean = false): Flow<PagingData<GamesResultModel>>

    /*suspend fun getProduct(id: Int): Flow<Resource<Product>>

    suspend fun getProductsByCategory(category: String): Flow<Resource<List<Product>>>

    suspend fun getCategories(): Flow<Resource<List<String>>>*/
}