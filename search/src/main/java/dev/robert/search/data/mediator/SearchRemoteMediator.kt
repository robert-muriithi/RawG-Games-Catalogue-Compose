package dev.robert.search.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.robert.database.database.GamesDatabase
import dev.robert.database.entities.GameEntity
import dev.robert.database.entities.RemoteKey
import dev.robert.network.apiservice.GamesApi
import dev.robert.search.data.mappers.toEntity
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * RemoteMediator for search results.
 */
@Deprecated("Not in use anymore.")
@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator(
    private val appDb: GamesDatabase,
    private val apiService: GamesApi,
    private val query: String? = null,
) : RemoteMediator<Int, GameEntity>() {

    private val remoteKeyDao = appDb.remoteKeyDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameEntity>,
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }

                LoadType.APPEND -> {
                    val remoteKey = appDb.withTransaction {
                        remoteKeyDao.getKeyByGame("_search")
                    } ?: return MediatorResult.Success(true)

                    if (remoteKey.next == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.next
                }
            }
            val apiResponse =
                apiService.getGames(
                    page = page,
                    pageSize = state.config.pageSize,
                    search = query,
                )
            val games = apiResponse.results.sortedByDescending { it.name }

            val endOfPaginationReached = games.isEmpty()
            appDb.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    appDb.gameEntityDao().deleteAllGames()
                }
                val nextPage = if (games.isEmpty()) {
                    null
                } else {
                    page?.plus(1)
                }

                remoteKeyDao.insert(
                    RemoteKey(
                        id = "_search",
                        next = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                appDb.gameEntityDao().insertGame(games.map {
                    it.toEntity(searchQuery = query)
                })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val remoteKey = appDb.withTransaction {
            remoteKeyDao.getKeyByGame("_search")
        } ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)

        return if ((System.currentTimeMillis() - remoteKey.lastUpdated) >= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}