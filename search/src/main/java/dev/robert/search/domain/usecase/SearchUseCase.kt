package dev.robert.search.domain.usecase

import dev.robert.search.domain.repository.SearchRepository

class SearchUseCase(private val repository: SearchRepository) {
    operator fun invoke(query: String) = repository.search(query)
}