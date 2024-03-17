package dev.robert.datastore.domain.usecase

import dev.robert.datastore.domain.repo.DataStoreRepo

class ThemeUseCase(private val repository: DataStoreRepo) {
    suspend fun saveTheme(theme: Int) {
        repository.saveTheme(theme)
    }
    fun getTheme() = repository.getTheme()
}