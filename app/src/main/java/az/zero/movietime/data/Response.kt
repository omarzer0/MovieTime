package az.zero.movietime.data

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Show>,
    @SerializedName("total_results") val total_results: Int,
    @SerializedName("total_pages") val total_pages: Int
)