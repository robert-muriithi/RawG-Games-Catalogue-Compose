package dev.robert.games.domain.usecase

import dev.robert.games.domain.repository.GamesRepository

class GetLocalGameUseCase (private val repository: GamesRepository) {
    operator fun invoke(id: Int) = repository.getLocalGameDetails(id)
}