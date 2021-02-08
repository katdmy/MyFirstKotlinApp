package com.katdmy.android.myfirstkotlinapp.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.katdmy.android.myfirstkotlinapp.MovieApplication
import com.katdmy.android.myfirstkotlinapp.model.Genre
import com.katdmy.android.myfirstkotlinapp.model.ModelsMapper
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.model.MoviesJsonList
import com.katdmy.android.myfirstkotlinapp.retrofit.RetrofitClient
import kotlinx.coroutines.*

class PeriodWorker(
    context: Context,
    parameters: WorkerParameters
) : Worker(context, parameters) {

    private val tmdbApi = RetrofitClient.tmdbApi

    override fun doWork(): Result {

        val sharedPreferences =
            applicationContext.getSharedPreferences("mode", Context.MODE_PRIVATE)
        val mode = if (sharedPreferences.contains("mode"))
            sharedPreferences.getString("mode", "Popular") else "Popular"
        val searchString = if (sharedPreferences.contains("searchString"))
            sharedPreferences.getString("searchString", "") else ""

        val moviesDao = (applicationContext as MovieApplication).db.moviesDao
        val modelsMapper = ModelsMapper()

        runBlocking {
            val movies = when (mode) {
                "Popular" -> processMoviesList(tmdbApi.getPopularMovies())
                "Now playing" -> processMoviesList(tmdbApi.getNowPlayingMovies())
                "Top rated" -> processMoviesList(tmdbApi.getTopRatedMovies())
                "Upcoming" -> processMoviesList(tmdbApi.getUpcomingMovies())
                "Search" -> processMoviesList(tmdbApi.searchMovies(searchString!!))
                else -> emptyList()
            }
            moviesDao.replaceMovies(
                modelsMapper.moviesFromModelToRoom(movies)
            )
        }
        return Result.success()
    }

    private suspend fun processMoviesList(jsonMovies: MoviesJsonList): List<Movie> {
        val conf = tmdbApi.getConfiguration()
        val jsonGenres = tmdbApi.getGenresList().genres ?: emptyList()
        val genres = jsonGenres.map { Genre(id = it.id ?: 0, name = it.name ?: "") }
        val genresMap = genres.associateBy { it.id }
        return jsonMovies.results.map { jsonMovie ->
            @Suppress("unused")
            (Movie(
                id = jsonMovie.id ?: 0,
                title = jsonMovie.title ?: "",
                overview = jsonMovie.overview ?: "",
                poster = conf.images?.secureBaseUrl + conf.images?.posterSizes?.get(4) + jsonMovie.posterPath,
                backdrop = conf.images?.secureBaseUrl + conf.images?.backdropSizes?.get(3) + jsonMovie.backdropPath,
                ratings = jsonMovie.voteAverage?.toFloat() ?: 0f,
                numberOfRatings = jsonMovie.voteCount ?: 0,
                minimumAge = if (jsonMovie.adult == true) 16 else 13,
                runtime = tmdbApi.getMovieDetails(jsonMovie.id ?: 0).runtime ?: 0,
                genres = jsonMovie.genreIds?.map {
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                } ?: emptyList(),
                actors = emptyList()
            ))
        }
    }

}