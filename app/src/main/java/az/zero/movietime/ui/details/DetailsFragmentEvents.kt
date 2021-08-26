package az.zero.movietime.ui.details

import az.zero.movietime.data.Movie
import az.zero.movietime.utils.ShowType

sealed class DetailsFragmentEvents {
    data class NavigateToDetailsFragmentWithMovie(val movie: Movie, val showType: ShowType) : DetailsFragmentEvents()
}