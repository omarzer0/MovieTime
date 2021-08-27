package az.zero.movietime.ui.search

import az.zero.movietime.data.Show
import az.zero.movietime.utils.ShowType

sealed class SearchFragmentEvents {
    data class NavigateToDetailsFragmentWithShow(val show: Show) : SearchFragmentEvents()
}