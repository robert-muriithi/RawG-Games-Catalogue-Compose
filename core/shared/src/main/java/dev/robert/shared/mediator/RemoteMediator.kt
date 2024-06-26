package dev.robert.shared.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.robert.database.database.GamesDatabase
import dev.robert.database.entities.GameEntity
import dev.robert.network.apiservice.GamesApi
import dev.robert.network.dto.dto.games.GamesResponseResult
import retrofit2.HttpException
import java.io.IOException



// Not usable in the current state TODO : Fix this, make this resuable for all entities

/*@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorHelper<T : Any>(
    private val appDb: GamesDatabase,
    private val entityClass: Class<T>,
    private val apiCall: suspend (Int) -> ApiResponse
) : RemoteMediator<Int, T>() {

    private val dao: Any by lazy {
        val daoName = "${entityClass.simpleName}Dao"
        val daoField = appDb::class.java.getDeclaredField(daoName)
        daoField.isAccessible = true
        daoField.get(appDb)!!
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, T>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        val id = entityClass.getDeclaredMethod("getId").invoke(lastItem) as Int
                        (id / state.config.pageSize) + 1
                    }
                }
            }

            val apiResponse = apiCall(page)
            val result = apiResponse.resultDtos as List<*>

            val mappedEntities = mapDataToEntities(result)

            appDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao::class.java.getDeclaredMethod("deleteAll").invoke(dao)
                }
                val daoMethod = dao::class.java.getDeclaredMethod("insertAll", List::class.java)
                daoMethod.invoke(dao, mappedEntities)
            }

            MediatorResult.Success(endOfPaginationReached = result.isEmpty())
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) { // Catch any other exception
            MediatorResult.Error(e)
        }
    }

    private  fun mapDataToEntities(data: List<*>): List<T?> {
        return data.map { it?.toEntityResults() }
    }

    private fun Any.toEntityResults(): T {
        val entityClass = this::class.java
        val entityName = entityClass.simpleName
        val entityDao = dao::class.java
        val entityMethod = entityDao.getDeclaredMethod("insert${entityName}s", entityClass)
        return entityMethod.invoke(dao, this) as T
    }
}*/
