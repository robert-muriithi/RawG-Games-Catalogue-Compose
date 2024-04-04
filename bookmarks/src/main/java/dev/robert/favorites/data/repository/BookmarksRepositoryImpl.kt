package dev.robert.favorites.data.repository

import dev.robert.database.database.GamesDatabase
import dev.robert.favorites.data.mappers.toDomain
import dev.robert.favorites.data.mappers.toDomainList
import dev.robert.favorites.domain.model.Game
import dev.robert.favorites.domain.repository.BookmarksRepository
import dev.robert.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class BookmarksRepositoryImpl(
    database: GamesDatabase,
) : BookmarksRepository {

    private val gameDao = database.gameEntityDao()
    override fun getBookmarks(): Flow<Resource<List<Game>>> {
        return try {
            val resultFromDb = gameDao.getBookmarkedGames()
            val games = resultFromDb.map { it.toDomainList() }
            games.map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            flow {
                emit(Resource.Failure(throwable))
            }
        }
    }
    override suspend fun clearBookmarks() {
        gameDao.clearBookmarks()
    }

    override suspend fun clearBookmark(id: Int, isBookmark: Boolean) {
        gameDao.updateBookmark(id, isBookmark)
    }
}