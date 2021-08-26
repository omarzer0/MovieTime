package az.zero.movietime.ui.home

import az.zero.movietime.data.Movie
import az.zero.movietime.utils.ShowType

sealed class HomeFragmentEvents {
    data class NavigateToDetailsFragmentWithMovie(val movie: Movie,val showType:ShowType) : HomeFragmentEvents()
}
