package com.katdmy.android.myfirstkotlinapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.katdmy.android.myfirstkotlinapp.model.*
import com.katdmy.android.myfirstkotlinapp.retrofit.TmdbApi
import com.katdmy.android.myfirstkotlinapp.room.MoviesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MoviesRepository(
    private val tmdbApi: TmdbApi,
    private val moviesDao: MoviesDao,
    private val modelsMapper: ModelsMapper
) {

    suspend fun loadMovies(
        mode: String,
        searchString: String
    ): List<Movie> {
        val movies =  when (mode) {
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
        return movies
    }

    suspend fun getMovieDetails(oldData: Movie): Movie {
        val conf = tmdbApi.getConfiguration()
        val jsonActors = tmdbApi.getMovieActors(oldData.id).cast ?: emptyList()
        val actors = jsonActors.subList(0,10).map { jsonActor ->
            @Suppress("unused")
            Actor(
                id = jsonActor.id ?: 0,
                name = jsonActor.name ?: "",
                picture = conf.images?.secureBaseUrl + conf.images?.profileSizes?.get(1) + jsonActor.profilePath
            )
        }
        moviesDao.replaceActors(
            modelsMapper.actorsFromModelToRoom(actors)
        )
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

    suspend fun observeMovieList(): Flow<List<Movie>> = withContext(Dispatchers.IO) {
        return@withContext moviesDao.getAllMovies()
            .map { modelsMapper.moviesFromRoomToModel(it) }
    }

    suspend fun getPagedMovies(page: Int): List<Movie> {
        val jsonMovies = tmdbApi.getPagedPopularMovies(page).await()
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
        //return moviesDao.pagedMovies().toLiveData(pageSize = 4).map { modelsMapper.moviesFromRoomToModel(it) }


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