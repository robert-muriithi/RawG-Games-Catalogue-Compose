package dev.robert.games.domain.usecase

import dev.robert.games.domain.repository.GamesRepository

class BookMarkGameUseCase (private val repository: GamesRepository) {
    operator fun invoke(id: Int, isBookMarked: Boolean) = repository.bookMarkGame(id, isBookMarked)
}