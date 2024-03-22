package dev.robert.games.domain.usecase

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow

class GetGenreGamesUseCase(private val repository: GamesRepository) {
    operator fun invoke(genre: String) : Flow<PagingData<GamesResultModel>> {
        return repository.getGenresGames(genres = genre)
    }
}