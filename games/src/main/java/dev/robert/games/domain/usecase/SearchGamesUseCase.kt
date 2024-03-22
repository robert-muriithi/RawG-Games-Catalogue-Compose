package dev.robert.games.domain.usecase

import androidx.paging.PagingData
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

class SearchGamesUseCase (private val repository: GamesRepository) {
    operator fun invoke(
        searchQuery: String,
    ) : Flow<PagingData<GamesResultModel>> {
        return repository.getGames(query = searchQuery)
    }
}