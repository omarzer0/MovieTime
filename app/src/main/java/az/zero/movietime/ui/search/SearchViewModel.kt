package az.zero.movietime.ui.search

import androidx.lifecycle.*
import androidx.paging.cachedIn
import az.zero.movietime.data.Show
import az.zero.movietime.repository.ShowRepository
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.SEARCH_QUERY
import az.zero.movietime.utils.START_SEARCH_QUERY
import az.zero.movietime.utils.ShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    val searchQuery = state.getLiveData(SEARCH_QUERY, START_SEARCH_QUERY)

    private val showFlow = searchQuery.asFlow().flatMapLatest { query ->
        showRepository.getShows(methodToCall = MethodToCall.SEARCH_SHOW, searchQuery = query)
            .cachedIn(viewModelScope)
    }

    val shows = showFlow.asLiveData()

    private val showEventChannel = Channel<SearchFragmentEvents>()
    val showEvent = showEventChannel.receiveAsFlow()

    fun showItemClicked(show: Show) = viewModelScope.launch {
        showEventChannel.send(
            SearchFragmentEvents.NavigateToDetailsFragmentWithShow(
                show
            )
        )
    }
}