package com.katdmy.android.myfirstkotlinapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.katdmy.android.myfirstkotlinapp.Notifications
import com.katdmy.android.myfirstkotlinapp.model.*
import com.katdmy.android.myfirstkotlinapp.retrofit.TmdbApi
import com.katdmy.android.myfirstkotlinapp.room.MoviesDao
import com.katdmy.android.myfirstkotlinapp.viewmodel.MovieDetailsState
import com.katdmy.android.myfirstkotlinapp.viewmodel.MovieListState
import kotlinx.coroutines.Dispatchers

class MoviesRepository(
    private val tmdbApi: TmdbApi,
    private val moviesDao: MoviesDao,
    private val modelsMapper: ModelsMapper,
    private val notifications: Notifications
) {
    private val TAG = MoviesRepository::class.java.simpleName

    init {
        notifications.initialize()
    }

    fun loadMovies(
        mode: String,
        searchString: String
    ): LiveData<MovieListState> = liveData(Dispatchers.IO) {
        emit(MovieListState.Loading)

        val cachedData = moviesDao.getAllMovies().map { modelsMapper.movieFromRoomToModel(it) }
        if (cachedData.isNotEmpty())
            emit(MovieListState.Data(cachedData))

        val movies = when (mode) {
            "Popular" -> processMoviesList(tmdbApi.getPopularMovies())
            "Now playing" -> processMoviesList(tmdbApi.getNowPlayingMovies())
            "Top rated" -> processMoviesList(tmdbApi.getTopRatedMovies())
            "Upcoming" -> processMoviesList(tmdbApi.getUpcomingMovies())
            "Search" -> processMoviesList(tmdbApi.searchMovies(searchString))
            else -> emptyList()
        }
        moviesDao.replaceMovies(
            modelsMapper.moviesFromModelToRoom(movies)
        )

        if (movies.isNotEmpty())
            emit(MovieListState.Data(movies))
        else
            emit(MovieListState.Empty)

        val topRatedMovie = movies.maxByOrNull { it.ratings }
        topRatedMovie?.let {
            notifications.showNotification(it)
        }

    }

    fun getMovieDetails(movie: Movie): LiveData<MovieDetailsState> = liveData(Dispatchers.IO) {
        emit(MovieDetailsState.LoadingActors(movie))

        val cachedData =
            moviesDao.getAllActors(movie.id).map { modelsMapper.actorFromRoomToModel(it) }
        if (cachedData.isNotEmpty())
            emit(MovieDetailsState.Data(movie, cachedData))

        val actors = getActors(movie.id)
        moviesDao.replaceActors(
            modelsMapper.actorsFromModelToRoom(actors, movie.id)
        )

        if (actors.isNotEmpty())
            emit(MovieDetailsState.Data(movie, actors))
        else
            emit(MovieDetailsState.EmptyActors(movie))
    }

    fun getMovieById(movieId: Int): LiveData<MovieDetailsState> = liveData(Dispatchers.IO) {
        lateinit var movie: Movie

        emit(MovieDetailsState.LoadingMovie)

        val cachedMovieData = moviesDao.getMovieById(movieId)
        if (cachedMovieData != null) {
            movie = modelsMapper.movieFromRoomToModel(cachedMovieData)
            emit(MovieDetailsState.LoadingActors(movie))
        } else {
            val jsonMovie = tmdbApi.getMovieDetails(movieId)
            if (jsonMovie == null)
                emit(MovieDetailsState.EmptyMovie)
            else {
                val conf = tmdbApi.getConfiguration()
                val jsonGenres = tmdbApi.getGenresList().genres ?: emptyList()
                val genres = jsonGenres.map { Genre(id = it.id ?: 0, name = it.name ?: "") }
                val genresMap = genres.associateBy { it.id }
                movie = Movie(
                    id = jsonMovie.id ?: 0,
                    title = jsonMovie.title ?: "",
                    overview = jsonMovie.overview ?: "",
                    poster = conf.images?.secureBaseUrl + conf.images?.posterSizes?.get(4) + jsonMovie.posterPath,
                    backdrop = conf.images?.secureBaseUrl + conf.images?.backdropSizes?.get(3) + jsonMovie.backdropPath,
                    ratings = jsonMovie.voteAverage?.toFloat() ?: 0f,
                    numberOfRatings = jsonMovie.voteCount ?: 0,
                    minimumAge = if (jsonMovie.adult == true) 16 else 13,
                    runtime = tmdbApi.getMovieDetails(jsonMovie.id ?: 0).runtime ?: 0,
                    genres = jsonMovie.genres?.map {
                        genresMap[it.id] ?: throw IllegalArgumentException("Genre not found")
                    } ?: emptyList(),
                    actors = emptyList()
                )
                emit(MovieDetailsState.LoadingActors(movie))
            }
        }

        if (movie == null)
            return@liveData

        val cachedActorsData =
            moviesDao.getAllActors(movie.id).map { modelsMapper.actorFromRoomToModel(it) }
        if (cachedActorsData.isNotEmpty())
            emit(MovieDetailsState.Data(movie, cachedActorsData))

        val actors = getActors(movie.id)
        moviesDao.replaceActors(
            modelsMapper.actorsFromModelToRoom(actors, movie.id)
        )

        if (actors.isNotEmpty())
            emit(MovieDetailsState.Data(movie, actors))
        else
            emit(MovieDetailsState.EmptyActors(movie))
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

    private suspend fun getActors(movieId: Int): List<Actor> {
        val conf = tmdbApi.getConfiguration()
        val jsonActors = tmdbApi.getMovieActors(movieId).cast ?: emptyList()
        return jsonActors.subList(0, 10).map { jsonActor ->
            @Suppress("unused")
            Actor(
                id = jsonActor.id ?: 0,
                name = jsonActor.name ?: "",
                picture = conf.images?.secureBaseUrl + conf.images?.profileSizes?.get(1) + jsonActor.profilePath
            )
        }
    }
}