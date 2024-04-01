package dev.robert.favorites.domain.usecase

import dev.robert.favorites.domain.repository.BookmarksRepository

class GetBookmarksUseCase (private val repository: BookmarksRepository){
    operator fun invoke() = repository.getBookmarks()
}