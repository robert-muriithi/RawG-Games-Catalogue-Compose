package dev.robert.favorites.domain.usecase

import dev.robert.favorites.domain.repository.BookmarksRepository

class DeleteBookmarkUseCase (private val repository: BookmarksRepository) {
    suspend operator fun invoke(id: Int, isBookmark : Boolean) = repository.clearBookmark(id, isBookmark)
}