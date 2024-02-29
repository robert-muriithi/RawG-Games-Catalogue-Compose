package dev.robert.games.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.robert.database.database.GamesDatabase
import dev.robert.database.entities.GameEntity
import dev.robert.database.entities.GenreEntity
import dev.robert.games.data.mappers.toDomain
import dev.robert.games.domain.model.game.GamesResponseModel
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.games.domain.repository.GamesRepository
import dev.robert.network.apiservice.GamesApi
import dev.robert.shared.ApiResponse
import dev.robert.shared.mediator.RemoteMediatorHelper
import dev.robert.shared.paging.PagingHelper
import dev.robert.shared.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GamesRepositoryImpl(
    private val gamesApi: GamesApi,
    private val appDb: GamesDatabase
) : GamesRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getGameGenres(): Flow<PagingData<Genre>> {
        val cachedGenres =  appDb.genreEntityDao().getAllGenres()
        val mediatorHelper = RemoteMediatorHelper(
            appDb = appDb,
            entityClass = GenreEntity::class.java,
            apiCall = {
                val response = ApiResponse(
                    resultDtos = gamesApi.getGameGenres()
                )
                response
            },
        )
        val pager = Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = mediatorHelper,
            pagingSourceFactory = { cachedGenres }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
        return pager.flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getGames(): Flow<PagingData<GamesResultModel>> = flow<PagingData<GamesResultModel>> {
        val cachedGames =  appDb.gameEntityDao().getAllGames()
        val mediatorHelper = RemoteMediatorHelper(
            appDb = appDb,
            entityClass = GameEntity::class.java,
            apiCall = {
                val response = ApiResponse(
                    resultDtos = safeApiCall {
                        gamesApi.getGames(
                            page = 1, pageSize = NETWORK_PAGE_SIZE
                        )
                    }
                )
                response
            },
        )
       Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = mediatorHelper,
            pagingSourceFactory = { cachedGames }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
        return@flow
    }.flowOn(Dispatchers.IO)

    override fun searchGames(query: String, searchExact: Boolean): Flow<PagingData<GamesResultModel>> = flow {
        val cachedGames = appDb.gameEntityDao().getGameByName(query)
        if(cachedGames.isNotEmpty()){
           flow {
                emit(cachedGames)
              }.map { pagingData ->
                pagingData.map { it.toDomain() }
           }
        } else {
            try{
               Pager(
                    config = PagingConfig(
                        pageSize = NETWORK_PAGE_SIZE,
                        maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        PagingHelper(
                            searchString = query,
                            apiCall = { page, search ->
                                val apiResponse = ApiResponse(
                                    resultDtos = safeApiCall {
                                        gamesApi.getGames(
                                            page = page,
                                            pageSize = NETWORK_PAGE_SIZE,
                                            search = search
                                        )
                                    }
                                )
                                apiResponse
                            }
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.map { it }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}


