package az.zero.movietime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.zero.movietime.data.Show
import az.zero.movietime.repository.ShowRepository
import az.zero.movietime.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private var methodToCall = state.get<MethodToCall>(METHOD_TO_CALL) ?: MethodToCall.GET_POPULAR
    private var showType = state.get<ShowType>(SHOW_TYPE) ?: ShowType.MOVIE

    lateinit var shows: LiveData<PagingData<Show>>
    fun getShows() {
        shows = when (methodToCall) {
            MethodToCall.TOP_RATED -> showRepository.getShows(MethodToCall.TOP_RATED, showType).cachedIn(viewModelScope)
            MethodToCall.TRENDING -> showRepository.getShows(MethodToCall.TRENDING, showType).cachedIn(viewModelScope)
            MethodToCall.UPCOMING -> showRepository.getShows(MethodToCall.UPCOMING, showType).cachedIn(viewModelScope)
            MethodToCall.AIRING_TODAY ->showRepository.getShows(MethodToCall.AIRING_TODAY, showType).cachedIn(viewModelScope)
            else -> showRepository.getShows(MethodToCall.GET_POPULAR, showType).cachedIn(viewModelScope)
        }.exhaustive
    }


    private val showEventChannel = Channel<HomeFragmentEvents>()
    val showEvent = showEventChannel.receiveAsFlow()

    fun showItemClicked(show: Show) = viewModelScope.launch {
        showEventChannel.send(
            HomeFragmentEvents.NavigateToDetailsFragmentWithShow(
                show,
                showType
            )
        )
    }
}