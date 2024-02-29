package dev.robert.games.domain.usecase

import dev.robert.games.domain.repository.GamesRepository

class GetProductById(private val repository: GamesRepository) {
//        suspend operator fun invoke(id: Int) = repository.getProduct(id)
}