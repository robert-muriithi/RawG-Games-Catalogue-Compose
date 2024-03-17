package dev.robert.datastore.domain.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {
    suspend fun saveTheme(theme: Int)
    fun getTheme(): Flow<Int>
}