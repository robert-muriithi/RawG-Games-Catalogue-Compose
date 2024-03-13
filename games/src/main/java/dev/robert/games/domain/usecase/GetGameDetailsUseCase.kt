package dev.robert.games.domain.usecase

import dev.robert.games.domain.repository.GamesRepository

class GetGameDetailsUseCase(private val repository: GamesRepository) {
    operator fun invoke(id: Int) = repository.getGameDetails(id)
}