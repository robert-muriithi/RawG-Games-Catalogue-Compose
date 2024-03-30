package dev.robert.search.domain.usecase

import dev.robert.search.domain.repository.SearchRepository

class AddToRecentSearchUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(id: Int, recentSearch: Boolean) = repository.updateRecentSearch(id, recentSearch)
}