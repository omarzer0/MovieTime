package az.zero.movietime.ui.details

import androidx.lifecycle.*
import androidx.paging.cachedIn
import az.zero.movietime.data.Show
import az.zero.movietime.repository.ShowRepository
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.SHOW
import az.zero.movietime.utils.SHOW_TYPE
import az.zero.movietime.utils.ShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    var show = state.getLiveData<Show>(SHOW)
    var showType = state.getLiveData<ShowType>(SHOW_TYPE, ShowType.MOVIE)

    private val similarMoviesFlow = showType.asFlow().flatMapLatest { showType ->
        showRepository.getShows(MethodToCall.GET_SIMILAR, showType, show.value?.id ?: -1)
            .cachedIn(viewModelScope)
    }

    val similarMovies = similarMoviesFlow.asLiveData()

    private val relatedMoviesFlow = showType.asFlow().flatMapLatest { showType ->
        showRepository.getShows(MethodToCall.GET_RECOMMENDED, showType, show.value?.id ?: -1)
            .cachedIn(viewModelScope)
    }
    val relatedMovies = relatedMoviesFlow.asLiveData()

    private val showEventChannel = Channel<DetailsFragmentEvents>()
    val showEvent = showEventChannel.receiveAsFlow()

    fun showItemClicked(show: Show) = viewModelScope.launch {
        showEventChannel.send(
            DetailsFragmentEvents.NavigateToDetailsFragmentWithShow(
                show
            )
        )
    }


}