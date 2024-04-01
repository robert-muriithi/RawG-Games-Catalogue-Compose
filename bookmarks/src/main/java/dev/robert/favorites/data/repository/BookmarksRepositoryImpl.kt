package dev.robert.favorites.data.repository

import dev.robert.database.database.GamesDatabase
import dev.robert.favorites.data.mappers.toDomain
import dev.robert.favorites.domain.model.Game
import dev.robert.favorites.domain.repository.BookmarksRepository
import dev.robert.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class BookmarksRepositoryImpl (
     database: GamesDatabase
) : BookmarksRepository {

    private val gameDao = database.gameEntityDao()
    override fun getBookmarks(): Flow<Resource<List<Game>>> = flow {
        gameDao.getBookmarkedGames().map {
            entityList -> entityList.map {
                it.toDomain()
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