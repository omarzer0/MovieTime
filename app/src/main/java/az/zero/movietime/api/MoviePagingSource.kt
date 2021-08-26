package az.zero.movietime.api

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.zero.movietime.data.Movie
import az.zero.movietime.data.Response
import az.zero.movietime.utils.API_KEY
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.ShowType
import az.zero.movietime.utils.exhaustive

const val STARTING_PAGE_NUMBER = 1

class MoviePagingSource(
    private val movieApi: MovieApi,
    private val methodToCall: MethodToCall,
    private val showType: ShowType,
    private val movieId: Int
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGE_NUMBER

        return try {
            //val response = movieApi.getPopularMovies(API_KEY, position)
            val response: Response = when (methodToCall) {
                MethodToCall.GET_POPULAR -> movieApi.getPopularMovies(show, API_KEY, position)
                MethodToCall.TOP_RATED -> movieApi.getTopRatedMovies(show, API_KEY, position)
                MethodToCall.TRENDING -> movieApi.getTrendingMovies(show, API_KEY, position)
                MethodToCall.GET_SIMILAR -> movieApi.getSimilarMovies(
                    show,
                    movieId,
                    API_KEY,
                    position
                )
                MethodToCall.GET_RECOMMENDED -> {
                    Log.e("TAG", "load: get rec $show $movieId")
                    movieApi.getRecommendedMovie(
                        show,
                        movieId,
                        API_KEY,
                        position
                    )
                }
                MethodToCall.UPCOMING -> movieApi.getUpcomingMovies(API_KEY, position)
                MethodToCall.AIRING_TODAY -> movieApi.getAiringTodayTV(API_KEY, position)
            }.exhaustive

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

    private val show = if (showType == ShowType.TV) "tv" else "movie"

}