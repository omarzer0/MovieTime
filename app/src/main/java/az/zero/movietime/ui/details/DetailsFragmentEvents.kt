package az.zero.movietime.ui.details

import az.zero.movietime.data.Show
import az.zero.movietime.utils.ShowType

sealed class DetailsFragmentEvents {
    data class NavigateToDetailsFragmentWithShow(val show: Show, val showType: ShowType) : DetailsFragmentEvents()
}