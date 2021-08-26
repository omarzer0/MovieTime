package az.zero.movietime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import az.zero.movietime.data.Movie
import az.zero.movietime.repository.MovieRepository
import az.zero.movietime.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val state: SavedStateHandle
) :
    ViewModel() {

    private var methodToCall = state.get<MethodToCall>(METHOD_TO_CALL) ?: MethodToCall.GET_POPULAR
    private var showType = state.get<ShowType>(SHOW_TYPE) ?: ShowType.MOVIE

    lateinit var shows: LiveData<PagingData<Movie>>
    fun getShows() {
        shows = when (methodToCall) {
            MethodToCall.TOP_RATED -> movieRepository.getShows(MethodToCall.TOP_RATED, showType)
            MethodToCall.TRENDING -> movieRepository.getShows(MethodToCall.TRENDING, showType)
            MethodToCall.UPCOMING -> movieRepository.getShows(MethodToCall.UPCOMING, showType)
            MethodToCall.AIRING_TODAY ->movieRepository.getShows(MethodToCall.AIRING_TODAY, showType)
            else -> movieRepository.getShows(MethodToCall.GET_POPULAR, showType)
        }.exhaustive
    }


    private val movieEventChannel = Channel<HomeFragmentEvents>()
    val movieEvent = movieEventChannel.receiveAsFlow()

    fun movieItemClicked(movie: Movie) = viewModelScope.launch {
        movieEventChannel.send(
            HomeFragmentEvents.NavigateToDetailsFragmentWithMovie(
                movie,
                showType
            )
        )
    }
}