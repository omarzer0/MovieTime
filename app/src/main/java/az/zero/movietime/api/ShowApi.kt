package az.zero.movietime.api

import az.zero.movietime.data.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowApi {

    @GET("{movieOrTV}/popular")
    suspend fun getPopularShows(
        @Path("movieOrTV") movieOrTV: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("{movieOrTV}/top_rated")
    suspend fun getTopRatedShows(
        @Path("movieOrTV") movieOrTV: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("trending/{movieOrTV}/week")
    suspend fun getTrendingShows(
        @Path("movieOrTV") movieOrTV: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("tv/airing_today")
    suspend fun getAiringTodayTV(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response


    // movie item dependent
    @GET("{movieOrTV}/{show_id}/similar")
    suspend fun getSimilarShows(
        @Path("movieOrTV") movieOrTV: String,
        @Path("show_id") showId: Int,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("{movieOrTV}/{show_id}/recommendations")
    suspend fun getRecommendedShows(
        @Path("movieOrTV") movieOrTV: String,
        @Path("show_id") showId: Int,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response
}

// https://api.themoviedb.org/3/movie/150689/recommendations?api_key=49066942591aa4286806177d1fc935a0&page=1
// https://api.themoviedb.org/3/movie/150689/recommendations?api_key=49066942591aa4286806177d1fc935a0&page=1