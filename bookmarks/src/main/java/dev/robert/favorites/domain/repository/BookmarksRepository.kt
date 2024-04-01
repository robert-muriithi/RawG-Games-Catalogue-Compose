package dev.robert.favorites.domain.repository

import dev.robert.favorites.domain.model.Game
import dev.robert.network.Resource
import kotlinx.coroutines.flow.Flow


interface BookmarksRepository {
    fun getBookmarks() : Flow<Resource<List<Game>>>

    suspend fun clearBookmarks()

    suspend fun clearBookmark(id: Int, isBookmark: Boolean)
}