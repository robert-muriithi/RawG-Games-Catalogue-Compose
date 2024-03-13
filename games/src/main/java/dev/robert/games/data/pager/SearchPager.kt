package dev.robert.games.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.robert.network.apiservice.GamesApi
import dev.robert.network.dto.dto.games.GamesResponseResult
import retrofit2.HttpException
import java.io.IOException

class SearchPager(
    private val apiService: GamesApi,
    private val searchQuery: String,
    private val searchExact: Boolean = false
): PagingSource<String, GamesResponseResult>() {
    override fun getRefreshKey(state: PagingState<String, GamesResponseResult>): String {
        return state.anchorPosition.toString()
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, GamesResponseResult> {
        val nextPage = params.key ?: ""
        val response = apiService.getGames(
            page = nextPage.toInt(),
            search = searchQuery,
            searchExact = searchExact
        )
        return try {
            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = response.next
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}