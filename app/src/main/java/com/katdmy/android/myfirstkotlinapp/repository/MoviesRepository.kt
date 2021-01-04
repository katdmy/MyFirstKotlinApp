package com.katdmy.android.myfirstkotlinapp.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.katdmy.android.myfirstkotlinapp.model.Actor
import com.katdmy.android.myfirstkotlinapp.model.Genre
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.model.MoviesJsonList
import com.katdmy.android.myfirstkotlinapp.retrofit.RetrofitClient.tmdbApi
import kotlinx.coroutines.*

object MoviesRepository {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Coroutine exception, scope active:${coroutineScope.isActive}", throwable)
    }

    private const val API_KEY = "37d023007af6569b99e1ba7cad35a94b"

    suspend fun getPopularMovies(): List<Movie> =
        processMoviesList(tmdbApi.getPopularMovies(API_KEY))

    suspend fun getNowPlayingMovies(): List<Movie> =
        processMoviesList(tmdbApi.getNowPlayingMovies(API_KEY))

    suspend fun getTopRatedMovies(): List<Movie> =
        processMoviesList(tmdbApi.getTopRatedMovies(API_KEY))

    suspend fun getUpcomingMovies(): List<Movie> =
        processMoviesList(tmdbApi.getUpcomingMovies(API_KEY))

    suspend fun searchMovies(query: String): List<Movie> =
        processMoviesList(tmdbApi.searchMovies(query, API_KEY))

    private suspend fun processMoviesList(jsonMovies: MoviesJsonList): List<Movie> {
        val conf = tmdbApi.getConfiguration(API_KEY)
        val jsonGenres = tmdbApi.getGenresList(API_KEY).genres ?: emptyList()
        val genres = jsonGenres.map { Genre(id = it.id ?: 0, name = it.name ?: "") }
        val genresMap = genres.associateBy { it.id }
        return jsonMovies.results.map { jsonMovie ->
            @Suppress("unused")
            Movie(
                id = jsonMovie.id ?: 0,
                title = jsonMovie.title ?: "",
                overview = jsonMovie.overview ?: "",
                poster = conf.images?.secureBaseUrl + conf.images?.posterSizes?.get(4) + jsonMovie.posterPath,
                backdrop = conf.images?.secureBaseUrl + conf.images?.backdropSizes?.get(3) + jsonMovie.backdropPath,
                ratings = jsonMovie.voteAverage?.toFloat() ?: 0f,
                numberOfRatings = jsonMovie.voteCount ?: 0,
                minimumAge = if (jsonMovie.adult == true) 16 else 13,
                runtime = tmdbApi.getMovieDetails(jsonMovie.id ?: 0, API_KEY).runtime ?: 0,
                genres = jsonMovie.genreIds?.map {
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                } ?: emptyList(),
                actors = emptyList()
            )
        }
    }

    suspend fun getMovieDetails(movie: Movie): Movie {
        val conf = tmdbApi.getConfiguration(API_KEY)
        val jsonActors = tmdbApi.getMovieActors(movie.id, API_KEY).cast ?: emptyList()
        val actors = jsonActors.map { jsonActor ->
            @Suppress("unused")
            Actor(
                id = jsonActor.id ?: 0,
                name = jsonActor.name ?: "",
                picture = conf.images?.secureBaseUrl + conf.images?.profileSizes?.get(1) + jsonActor.profilePath
            )
        }
        movie.actors = actors
        return movie
    }
}