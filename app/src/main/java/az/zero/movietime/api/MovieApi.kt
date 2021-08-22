package az.zero.movietime.api

import az.zero.movietime.data.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
//        @Path("movieOrTV") movieOrTV: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response
}