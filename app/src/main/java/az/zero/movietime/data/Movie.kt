package az.zero.movietime.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat

@Parcelize
data class Movie(
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String?,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val original_title: String,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("title") val title: String,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("vote_count") val vote_count: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val vote_average: Double
) : Parcelable {
    val moviePoster get() = "https://image.tmdb.org/t/p/w500/$poster_path"
    val movieBackPoster get() = "https://image.tmdb.org/t/p/w500/$backdrop_path"
    val voteAverageWithOneDecimalPlace get() = DecimalFormat("#.#").format(vote_average).toDouble()
    fun getReleaseDateYear(): String {
        return when {
            release_date == null -> "UnKnown"
            release_date.length < 4 -> "UnKnown"
            else -> release_date.slice(0..3)
        }
    }


    //// toString can be used in nav_graph to set the movie title to appbar text
    //// instead of passing another parameter (movie title)
    //override fun toString(): String = title
}

