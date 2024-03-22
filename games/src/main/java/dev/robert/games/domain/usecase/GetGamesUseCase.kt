package dev.robert.games.domain.usecase

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.GameGenre
import dev.robert.games.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow

class GetGamesUseCase(private val repository: GamesRepository) {
         operator fun invoke(query: String?) : Flow<PagingData<GamesResultModel>> {
            return repository.getGames(query = query)
        }
}