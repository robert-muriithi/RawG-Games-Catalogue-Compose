package dev.robert.games.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.robert.database.database.GamesDatabase
import dev.robert.database.entities.GameEntity
import dev.robert.database.entities.GenreEntity
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
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GenreEntity>,
    ): MediatorResult {
        return try {
            val page = when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->{
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null){
                        1
                    }else{
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            val apiResponse = apiService.getGameGenres(
                page = page
            )
            val genres = apiResponse.genreResponses

            appDb.withTransaction {
                if(loadType == LoadType.REFRESH){
                    appDb.genreEntityDao().deleteAll()
                }
                val genresEntity = genres.map { it.toEntity() }
                appDb.genreEntityDao().insertAll(genresEntity)
            }
            MediatorResult.Success(endOfPaginationReached = genres.isEmpty())
        }catch (e: HttpException){
            MediatorResult.Error(e)
        }
        catch (e: IOException){
            MediatorResult.Error(e)
        }
    }
}






