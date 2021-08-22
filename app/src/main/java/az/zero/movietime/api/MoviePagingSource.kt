package az.zero.movietime.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.zero.movietime.data.Movie
import az.zero.movietime.utils.API_KEY

const val STARTING_PAGE_NUMBER = 1

class MoviePagingSource(
    private val movieApi: MovieApi
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGE_NUMBER

        return try {
            val response = movieApi.getPopularMovies(API_KEY, position)
            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_PAGE_NUMBER) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = state.anchorPosition
}