package dev.robert.games.domain.usecase

import dev.robert.games.domain.repository.GamesRepository

class GetProductCategory(private val repository: GamesRepository) {
//        suspend operator fun invoke(category: String) = repository.getProductsByCategory(category)
}