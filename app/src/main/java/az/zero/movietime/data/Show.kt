package az.zero.movietime.data

import android.os.Parcelable
import az.zero.movietime.utils.UNKNOWN
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat

@Parcelize
data class Show(
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String?,
    @SerializedName("first_air_date") val first_air_date: String?,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val original_title: String,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("vote_count") val vote_count: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val vote_average: Double
) : Parcelable {
    val showPoster get() = "https://image.tmdb.org/t/p/w500/$poster_path"
    val showBackPoster get() = "https://image.tmdb.org/t/p/w500/$backdrop_path"
    val voteAverageWithOneDecimalPlace get() = DecimalFormat("#.#").format(vote_average).toDouble()
    val fullReleaseDate get() = release_date ?: first_air_date ?: UNKNOWN
    val showTitle get() = title ?: name ?: UNKNOWN
    val descriptionOrEmpty get() = if (overview.isEmpty()) "No Overview" else overview
    fun getTheYearOfReleaseDateYear() =
        if (release_date != null && release_date.length > 4) release_date.slice(0..3)
        else if (first_air_date != null && first_air_date.length > 4) first_air_date.slice(0..3)
        else UNKNOWN


    //// toString can be used in nav_graph to set the movie title to appbar text
    //// instead of passing another parameter (movie title)
    //override fun toString(): String = title
}

