package dev.robert.games.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.robert.database.database.GamesDatabase
import dev.robert.database.entities.GameEntity
import dev.robert.database.entities.GenreEntity
import dev.robert.database.entities.RemoteKey
import dev.robert.games.data.mappers.toEntity
import dev.robert.games.data.mappers.toGenresDomain
import dev.robert.network.apiservice.GamesApi
import dev.robert.network.dto.dto.category.GenreResponse
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GenresRemoteMediator(
    private val appDb: GamesDatabase,
    private val apiService: GamesApi
) : RemoteMediator<Int, GenreEntity>() {
    private val remoteKeyDao = appDb.remoteKeyDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GenreEntity>,
    ): MediatorResult {
        try {
            val page= when(loadType) {
                LoadType.REFRESH -> {
                    1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKey = appDb.withTransaction {
                        remoteKeyDao.getKeyByGame("_genre")
                    } ?: return MediatorResult.Success(true)

                    if(remoteKey.next == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.next
                }
            }
            val apiResponse = apiService.getGameGenres(
                page = page
            )
            val genres = apiResponse.genreResponses.sortedByDescending { it.name }

            val endOfPaginationReached = genres.isEmpty()
            appDb.withTransaction {

                if(loadType == LoadType.REFRESH) {
                    appDb.gameEntityDao().deleteAllGames()
                }
                val nextPage = if(genres.isEmpty()) {
                    null
                } else {
                    page?.plus(1)
                }

                remoteKeyDao.insert(
                    RemoteKey(
                    id = "_genre",
                    next = nextPage,
                    lastUpdated = System.currentTimeMillis()
                )
                )
                appDb.genreEntityDao().insertAll(genres.map {
                    it.toEntity()
                })
            }
            return  MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e: HttpException){
            return MediatorResult.Error(e)
        }
        catch (e: IOException){
            return MediatorResult.Error(e)
        }
    }
}






