package az.zero.movietime.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import az.zero.movietime.data.Movie
import az.zero.movietime.data.Response
import az.zero.movietime.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    val movies = movieRepository.getPopularMovies().cachedIn(viewModelScope)
}