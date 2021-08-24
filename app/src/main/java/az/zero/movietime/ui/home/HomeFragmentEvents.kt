package az.zero.movietime.ui.home

import az.zero.movietime.data.Movie

sealed class HomeFragmentEvents {
    data class NavigateToDetailsFragmentWithMovie(val movie: Movie) : HomeFragmentEvents()
}
