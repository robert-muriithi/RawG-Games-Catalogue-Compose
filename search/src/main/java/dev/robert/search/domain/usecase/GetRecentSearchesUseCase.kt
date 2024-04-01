package dev.robert.search.domain.usecase

import dev.robert.search.domain.repository.SearchRepository

class GetRecentSearchesUseCase(private val repository: SearchRepository) {
    operator fun invoke() = repository.getRecentSearches()
}