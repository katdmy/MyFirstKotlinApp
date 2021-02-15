package com.katdmy.android.myfirstkotlinapp.retrofit

import com.katdmy.android.myfirstkotlinapp.model.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("configuration")
    suspend fun getConfiguration(): ConfigurationJson

    @GET("movie/popular?language=en-US&page=1")
    suspend fun getPopularMovies(): MoviesJsonList

    @GET("movie/now_playing?language=en-US&page=1")
    suspend fun getNowPlayingMovies(): MoviesJsonList

    @GET("movie/top_rated?language=en-US&page=1")
    suspend fun getTopRatedMovies(): MoviesJsonList

    @GET("movie/upcoming?language=en-US&page=1")
    suspend fun getUpcomingMovies(): MoviesJsonList

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetails

    @GET("movie/{movie_id}/credits?language=en-US")
    suspend fun getMovieActors(@Path("movie_id") movieId: Int): ActorsJsonList

    @GET("genre/movie/list")
    suspend fun getGenresList(): GenresJsonList

    @GET("search/movie?include_adult=true")
    suspend fun searchMovies(@Query("query") query: String): MoviesJsonList
}