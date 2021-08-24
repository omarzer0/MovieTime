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

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendedMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response
}

// https://api.themoviedb.org/3/movie/150689/recommendations?api_key=49066942591aa4286806177d1fc935a0&page=1
// https://api.themoviedb.org/3/movie/150689/recommendations?api_key=49066942591aa4286806177d1fc935a0&page=1