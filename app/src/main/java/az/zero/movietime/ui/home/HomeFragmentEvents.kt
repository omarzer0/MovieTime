package az.zero.movietime.ui.home

import az.zero.movietime.data.Show
import az.zero.movietime.utils.ShowType

sealed class HomeFragmentEvents {
    data class NavigateToDetailsFragmentWithShow(val show: Show, val showType:ShowType) : HomeFragmentEvents()
}
