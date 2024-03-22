package dev.robert.games.domain.usecase

import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.repository.GamesRepository
import dev.robert.network.Resource
import kotlinx.coroutines.flow.Flow

class GetHotGamesUseCase(private val repository: GamesRepository) {
     operator fun invoke(
         refresh: Boolean
     ) : Flow<Resource<List<GamesResultModel>>>{
        return repository.getHotGames(refresh = refresh)
    }
}