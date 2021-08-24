package az.zero.movietime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.zero.movietime.data.Movie
import az.zero.movietime.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    init {
        getMovies()
    }

    //val movies = movieRepository.getPopularMovies().cachedIn(viewModelScope)
    lateinit var movies: LiveData<PagingData<Movie>>
    private fun getMovies() {
        movies = movieRepository.getPopularMovies().cachedIn(viewModelScope)
    }


    private val movieEventChannel = Channel<HomeFragmentEvents>()
    val movieEvent = movieEventChannel.receiveAsFlow()

    fun movieItemClicked(movie: Movie) = viewModelScope.launch {
        movieEventChannel.send(HomeFragmentEvents.NavigateToDetailsFragmentWithMovie(movie))
    }
}