package dev.robert.games.domain.usecase

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.GameGenre
import dev.robert.games.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow

class GetGamesUseCase(private val repository: GamesRepository) {
        suspend operator fun invoke() : Flow<PagingData<GamesResultModel>> {
            return repository.getGames()
        }
}