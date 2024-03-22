package dev.robert.games.domain.repository

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.game_details.GameDetailsModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.network.Resource
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    fun getGameGenres() : Flow<PagingData<Genre>>

    fun getGames(query: String?) : Flow<PagingData<GamesResultModel>>
    fun getGenresGames(genres : String?) : Flow<PagingData<GamesResultModel>>

    fun getHotGames(refresh : Boolean) : Flow<Resource<List<GamesResultModel>>>

    fun getGameDetails(id: Int) : Flow<Resource<GameDetailsModel>>

    fun getLocalGameDetails(id: Int) : Flow<GamesResultModel>

    fun bookMarkGame(id: Int, isBookMarked: Boolean) : Flow<Resource<Boolean>>

    fun getBookmarkedGames() : Flow<PagingData<GamesResultModel>>

    /*suspend fun getProduct(id: Int): Flow<Resource<Product>>

    suspend fun getProductsByCategory(category: String): Flow<Resource<List<Product>>>

    suspend fun getCategories(): Flow<Resource<List<String>>>*/
}