package az.zero.movietime.ui.search

import androidx.lifecycle.*
import androidx.paging.cachedIn
import az.zero.movietime.data.Show
import az.zero.movietime.repository.ShowRepository
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.SEARCH_QUERY_KEY
import az.zero.movietime.utils.START_SEARCH_QUERY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val searchQuery = state.getLiveData(SEARCH_QUERY_KEY, START_SEARCH_QUERY)

    val shows = searchQuery.switchMap { query ->
        showRepository.getShows(methodToCall = MethodToCall.SEARCH_SHOW, searchQuery = query)
            .asLiveData().cachedIn(viewModelScope)
    }

    private val showEventChannel = Channel<SearchFragmentEvents>()
    val showEvent = showEventChannel.receiveAsFlow()

    fun showItemClicked(show: Show) = viewModelScope.launch {
        showEventChannel.send(
            SearchFragmentEvents.NavigateToDetailsFragmentWithShow(
                show
            )
        )
    }

    fun updateSearchQuery(newQuery: String) {
        state[SEARCH_QUERY_KEY] = newQuery
    }

    fun getCurrentSearchQuery() = searchQuery.value
}