package az.zero.movietime.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import az.zero.movietime.data.Show
import az.zero.movietime.repository.ShowRepository
import az.zero.movietime.utils.METHOD_TO_CALL
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.SHOW_TYPE
import az.zero.movietime.utils.ShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val state: SavedStateHandle
) :
    ViewModel() {

    private val methodToCall = state.getLiveData(METHOD_TO_CALL, MethodToCall.GET_POPULAR)
    private val showType = state.getLiveData(SHOW_TYPE, ShowType.MOVIE)

    val shows = methodToCall.switchMap {
        showRepository.getShows(it, showType.value ?: ShowType.MOVIE).asLiveData()
            .cachedIn(viewModelScope)
    }


    private val showEventChannel = Channel<HomeFragmentEvents>()
    val showEvent = showEventChannel.receiveAsFlow()
    fun showItemClicked(show: Show) = viewModelScope.launch {
        showEventChannel.send(
            HomeFragmentEvents.NavigateToDetailsFragmentWithShow(
                show
            )
        )
    }
}