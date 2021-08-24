package az.zero.movietime.ui.details

import az.zero.movietime.data.Movie

sealed class DetailsFragmentEvents {
    data class NavigateToDetailsFragmentWithMovie(val movie: Movie) : DetailsFragmentEvents()
}