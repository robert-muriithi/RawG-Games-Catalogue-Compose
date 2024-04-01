package dev.robert.favorites.domain.usecase

import dev.robert.favorites.domain.repository.BookmarksRepository

class ClearBookmarksUseCase (private val repository: BookmarksRepository) {
    suspend operator fun invoke() = repository.clearBookmarks()
}