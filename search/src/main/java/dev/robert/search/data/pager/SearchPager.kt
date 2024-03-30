package dev.robert.search.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.robert.network.apiservice.GamesApi
import dev.robert.network.dto.dto.games.GamesResponseResult

class SearchPager (
    private val searchString: String,
    private val searchExact : Boolean,
    private val api: GamesApi
) : PagingSource<Int, GamesResponseResult>() {
    override fun getRefreshKey(state: PagingState<Int, GamesResponseResult>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GamesResponseResult> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.getGames( search = searchString, searchExact =  searchExact,  page = nextPageNumber)
            LoadResult.Page(
                data = response.results,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (response.results.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}