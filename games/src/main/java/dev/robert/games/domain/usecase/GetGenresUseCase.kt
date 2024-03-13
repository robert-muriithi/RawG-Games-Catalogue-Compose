package dev.robert.games.domain.usecase

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.games.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow

class GetGenresUseCase (private val repository: GamesRepository) {
    operator fun invoke() : Flow<PagingData<Genre>> {
        return repository.getGameGenres()
    }
}