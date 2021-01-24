package com.katdmy.android.myfirstkotlinapp.repository

import com.katdmy.android.myfirstkotlinapp.model.Actor
import com.katdmy.android.myfirstkotlinapp.model.Genre
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.model.MoviesJsonList
import com.katdmy.android.myfirstkotlinapp.retrofit.RetrofitClient
import com.katdmy.android.myfirstkotlinapp.retrofit.TmdbApi
import com.katdmy.android.myfirstkotlinapp.room.MoviesDao

class MoviesRepository(
    private val tmdbApi: TmdbApi,
    private val moviesDao: MoviesDao
) {

    suspend fun getPopularMovies(): List<Movie> =
        processMoviesList(tmdbApi.getPopularMovies())

    suspend fun getNowPlayingMovies(): List<Movie> =
        processMoviesList(tmdbApi.getNowPlayingMovies())

    suspend fun getTopRatedMovies(): List<Movie> =
        processMoviesList(tmdbApi.getTopRatedMovies())

    suspend fun getUpcomingMovies(): List<Movie> =
        processMoviesList(tmdbApi.getUpcomingMovies())

    suspend fun searchMovies(query: String): List<Movie> =
        processMoviesList(tmdbApi.searchMovies(query))

    suspend fun getMovieDetails(oldData: Movie): Movie {
        val conf = tmdbApi.getConfiguration()
        val jsonActors = tmdbApi.getMovieActors(oldData.id).cast ?: emptyList()
        val actors = jsonActors.map { jsonActor ->
            @Suppress("unused")
            Actor(
                id = jsonActor.id ?: 0,
                name = jsonActor.name ?: "",
                picture = conf.images?.secureBaseUrl + conf.images?.profileSizes?.get(1) + jsonActor.profilePath
            )
        }
        return Movie(
            oldData.id,
            oldData.title,
            oldData.overview,
            oldData.poster,
            oldData.backdrop,
            oldData.ratings,
            oldData.numberOfRatings,
            oldData.minimumAge,
            oldData.runtime,
            oldData.genres,
            actors
        )
    }

    private suspend fun processMoviesList(jsonMovies: MoviesJsonList): List<Movie> {
        val conf = tmdbApi.getConfiguration()
        val jsonGenres = tmdbApi.getGenresList().genres ?: emptyList()
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
                runtime = tmdbApi.getMovieDetails(jsonMovie.id ?: 0).runtime ?: 0,
                genres = jsonMovie.genreIds?.map {
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                } ?: emptyList(),
                actors = emptyList()
            )
        }
    }
}