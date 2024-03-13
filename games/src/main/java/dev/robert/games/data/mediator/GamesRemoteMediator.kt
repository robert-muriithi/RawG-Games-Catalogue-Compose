package dev.robert.games.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.robert.database.database.GamesDatabase
import dev.robert.database.entities.GameEntity
import dev.robert.games.data.mappers.toEntity
import dev.robert.network.apiservice.GamesApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GamesRemoteMediator(
    private val appDb: GamesDatabase,
    private val apiService: GamesApi,
) : RemoteMediator<Int, GameEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameEntity>,
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

            val apiResponse =
                apiService.getGames(
                page = page,
                pageSize = state.config.pageSize
            )
            val games = apiResponse.results

            appDb.withTransaction {
                if(loadType == LoadType.REFRESH){
                    appDb.gameEntityDao().deleteAllGames()
                }
                val gamesEntity = games.map { it.toEntity() }
                appDb.gameEntityDao().insertGame(gamesEntity)
            }
            MediatorResult.Success(endOfPaginationReached = games.isEmpty())
        }catch (e: HttpException){
            MediatorResult.Error(e)
        }
        catch (e: IOException){
            MediatorResult.Error(e)
        }
    }
}