package com.katdmy.android.myfirstkotlinapp.retrofit

import com.katdmy.android.myfirstkotlinapp.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("configuration")
    suspend fun getConfiguration(@Query("api_key") apiKey: String) : ConfigurationJson

    @GET("movie/popular?language=en-US&page=1")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String) : MoviesJsonList

    @GET("movie/now_playing?language=en-US&page=1")
    suspend fun getNowPlayingMovies(@Query("api_key") apiKey: String) : List<Movie>

    @GET("movie/top_rated?language=en-US&page=1")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String) : List<Movie>

    @GET("movie/upcoming?language=en-US&page=1")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String) : List<Movie>

    @GET("movie/{movie_id}") //GET/movie/{movie_id} - for receiving movie details information.
    suspend fun getMovieDetails(
       @Path("movie_id") movieId: Int,
       @Query("api_key") apiKey: String
    ) : MovieDetails

    @GET("movie/{movie_id}/credits?language=en-US")
    suspend fun getMovieActors(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ) :  ActorsJsonList

    @GET("genre/movie/list")
    suspend fun getGenresList(@Query("api_key") apiKey: String) : GenresJsonList

     //GET/person/{person_id} - for receiving information about some people.
     //GET/search/movie
 }