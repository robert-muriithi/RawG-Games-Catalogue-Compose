package dev.robert.shared.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.robert.shared.ApiResponse
import retrofit2.HttpException

/*
// Toodo: 23/02/2024
*/
// Missing something here!! A way to map the Api Response to the Dto and get the list of entities, count the number of pages and the number of items per page
class PagingHelper<T : Any>(
    private val searchString: String? = null,
    private val apiCall: suspend (Int, String) -> ApiResponse,
) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val apiResponse = apiCall.invoke(page, searchString ?: "")
            if (apiResponse.isSuccessful.not()) {
                return LoadResult.Error(Exception("Error"))
            }
            val result = apiResponse.resultDtos
            val resultDto = getResults(result)
            LoadResult.Page(
                data = resultDto,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (resultDto.isEmpty()) null else page + 1
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getResults(input: Any): List<T> {
       // val results = this::class.java.declaredMethods.find { it.name == "getResults" }
        val results = input::class.java.declaredMethods.find { it.name == "getResults" }
        return results?.invoke(input) as? List<T> ?: emptyList()
    }
}
