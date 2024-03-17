package dev.robert.games.domain.repository

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.game_details.GameDetailsModel
import dev.robert.games.domain.model.genre.GameGenre
import dev.robert.games.domain.model.genre.Genre
import dev.robert.shared.utils.Resource
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    fun getGameGenres() : Flow<PagingData<Genre>>

    fun getGames() : Flow<PagingData<GamesResultModel>>

    fun searchGames(query: String, searchExact: Boolean = false): Flow<PagingData<GamesResultModel>>

    fun getHotGames() : Flow<Resource<List<GamesResultModel>>>

    fun getGameDetails(id: Int) : Flow<Resource<GameDetailsModel>>

    fun getLocalGameDetails(id: Int) : Flow<GamesResultModel>

    fun bookMarkGame(id: Int, isBookMarked: Boolean) : Flow<Resource<Boolean>>

    /*suspend fun getProduct(id: Int): Flow<Resource<Product>>

    suspend fun getProductsByCategory(category: String): Flow<Resource<List<Product>>>

    suspend fun getCategories(): Flow<Resource<List<String>>>*/
}