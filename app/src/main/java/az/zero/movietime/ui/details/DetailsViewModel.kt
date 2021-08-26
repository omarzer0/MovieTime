package az.zero.movietime.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.zero.movietime.data.Movie
import az.zero.movietime.repository.MovieRepository
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.SHOW_TYPE
import az.zero.movietime.utils.ShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val movieEventChannel = Channel<DetailsFragmentEvents>()
    val movieEvent = movieEventChannel.receiveAsFlow()

    var movie = state.get<Movie>("movie")
    var showType = state.get<ShowType>(SHOW_TYPE) ?: ShowType.TV

    lateinit var similarMovies: LiveData<PagingData<Movie>>
    fun getSimilarMovies(movieId: Int) {
        similarMovies = movieRepository.getShows(MethodToCall.GET_SIMILAR, showType, movieId)
            .cachedIn(viewModelScope)
    }

    lateinit var relatedMovies: LiveData<PagingData<Movie>>
    fun getRecommendedMovies(movieId: Int) {
        relatedMovies = movieRepository.getShows(MethodToCall.GET_RECOMMENDED, showType, movieId)
            .cachedIn(viewModelScope)
    }

    fun movieItemClicked(movie: Movie) = viewModelScope.launch {
        movieEventChannel.send(
            DetailsFragmentEvents.NavigateToDetailsFragmentWithMovie(
                movie,
                showType
            )
        )
    }


}