package dev.robert.games.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.robert.database.database.GamesDatabase
import dev.robert.games.data.mappers.toDomain
import dev.robert.games.data.mappers.toGameResultModel
import dev.robert.games.data.mappers.toModel
import dev.robert.games.data.mediator.GamesRemoteMediator
import dev.robert.games.data.mediator.GenresRemoteMediator
import dev.robert.games.data.pager.SearchPager
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.game_details.GameDetailsModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.games.domain.repository.GamesRepository
import dev.robert.network.apiservice.GamesApi
import dev.robert.shared.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GamesRepositoryImpl(
    private val gamesApi: GamesApi,
    private val appDb: GamesDatabase,
) : GamesRepository {

    override fun getGameGenres(): Flow<PagingData<Genre>> {
        val cachedGenres = { appDb.genreEntityDao().getAllGenres() }
        /*val mediatorHelper = RemoteMediatorHelper(
            appDb = appDb,
            entityClass = GenreEntity::class.java,
            apiCall = {
                val response = ApiResponse(
                    resultDtos = gamesApi.getGameGenres()
                )
                response
            },
        )*/
        val genresRemoteMediator = GenresRemoteMediator(
            appDb = appDb,
            apiService = gamesApi
        )
        val pager = Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = genresRemoteMediator,
            pagingSourceFactory = cachedGenres
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
        return pager.flowOn(Dispatchers.IO)
    }

    override fun getGames(): Flow<PagingData<GamesResultModel>> {
        val cachedGames = {
            appDb.gameEntityDao().getAllGames()
        }
        val gamesRemoteMediator = GamesRemoteMediator(
            appDb = appDb,
            apiService = gamesApi
        )
        val pager = Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = gamesRemoteMediator,
            pagingSourceFactory = cachedGames
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
        return pager.flowOn(Dispatchers.IO)
    }

    override fun getHotGames(): Flow<Resource<List<GamesResultModel>>> = flow {
        try {
            val response = gamesApi.getGames(
                page = 1,
                pageSize = 10,
                ordering = "-rating"
            )
            Timber.d("getHotGames: $response")
            val games = response.results.map { it.toGameResultModel() }
            emit(Resource.Success(games))
        } catch (e: HttpException) {
            emit(Resource.Failure(e))
        } catch (e: IOException) {
            emit(Resource.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getGameDetails(id: Int): Flow<Resource<GameDetailsModel>> = flow {
        try {
            val response = gamesApi.getGameDetails(id)
            val gameDetails = response.toModel()
            emit(Resource.Success(gameDetails))
        } catch (e: HttpException) {
            emit(Resource.Failure(e))
        } catch (e: IOException) {
            emit(Resource.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getLocalGameDetails(id: Int): Flow<GamesResultModel> = flow {
        try {
            val game = appDb.gameEntityDao().getGameById(id)
            emit(game.toDomain())
        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(Dispatchers.IO)

    override fun bookMarkGame(id: Int, isBookMarked : Boolean): Flow<Resource<Boolean>> = flow {
        try{
            val game = appDb.gameEntityDao().getGameById(id)
            val updatedGame = game.copy(isBookMarked = isBookMarked)
            appDb.gameEntityDao().updateBookmark(id = id, bookmarked = !game.isBookMarked)
            emit(Resource.Success(!game.isBookMarked))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    /* override fun getHotGames(): Flow<PagingData<GamesResultModel>> {
        val cachedGames =  {
            appDb.gameEntityDao().getAllGames()
        }
        val gamesRemoteMediator = GamesRemoteMediator(
            appDb = appDb,
            apiService = gamesApi,
            dateRange = getDateRange(range = Range.MONTH, isPast = true),
            ordering = "-rating"
        )
        val pager = Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 10 + (10 * 2),
                enablePlaceholders = false
            ),
            remoteMediator = gamesRemoteMediator,
            pagingSourceFactory = cachedGames
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
        return pager.flowOn(Dispatchers.IO)
    }*/

    override fun searchGames(
        query: String,
        searchExact: Boolean,
    ): Flow<PagingData<GamesResultModel>> = flow<PagingData<GamesResultModel>> {
        val searchPager = SearchPager(
            apiService = gamesApi,
            searchQuery = query,
            searchExact = searchExact
        )
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { searchPager }
        ).flow.map { pagingData ->
            pagingData.map { it.toGameResultModel() }
        }
        return@flow
    }.flowOn(Dispatchers.IO)


    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}


