package az.zero.movietime.ui.details

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.zero.movietime.data.Show
import az.zero.movietime.repository.ShowRepository
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.SHOW
import az.zero.movietime.utils.SHOW_TYPE
import az.zero.movietime.utils.ShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val showEventChannel = Channel<DetailsFragmentEvents>()
    val showEvent = showEventChannel.receiveAsFlow()

    var show = state.get<Show>(SHOW)
    var showType = state.get<ShowType>(SHOW_TYPE) ?: ShowType.TV

    lateinit var similarMovies: LiveData<PagingData<Show>>
    fun getSimilarShows(movieId: Int) {
        similarMovies =
            showRepository.getShows(MethodToCall.GET_SIMILAR, showType, movieId).asLiveData()
                .cachedIn(viewModelScope)
    }

    lateinit var relatedMovies: LiveData<PagingData<Show>>
    fun getRecommendedShows(movieId: Int) {
        relatedMovies =
            showRepository.getShows(MethodToCall.GET_RECOMMENDED, showType, movieId).asLiveData()
                .cachedIn(viewModelScope)
    }

    fun showItemClicked(show: Show) = viewModelScope.launch {
        showEventChannel.send(
            DetailsFragmentEvents.NavigateToDetailsFragmentWithShow(
                show,
                showType
            )
        )
    }


}