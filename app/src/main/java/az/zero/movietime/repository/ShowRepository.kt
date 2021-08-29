package az.zero.movietime.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import az.zero.movietime.api.MoviePagingSource
import az.zero.movietime.api.ShowApi
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.ShowType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowRepository @Inject constructor(
    private val showApi: ShowApi
) {
    fun getShows(
        methodToCall: MethodToCall = MethodToCall.GET_POPULAR,
        showType: ShowType = ShowType.MOVIE,
        showId: Int = -1,
        searchQuery: String = ""
    ) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = { MoviePagingSource(showApi, methodToCall, showType, showId,searchQuery) }
    ).flow
}