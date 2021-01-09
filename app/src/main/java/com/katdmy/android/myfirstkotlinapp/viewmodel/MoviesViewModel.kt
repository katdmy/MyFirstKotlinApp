package com.katdmy.android.myfirstkotlinapp.viewmodel

import androidx.lifecycle.*
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val handle: SavedStateHandle,
    private val repo: MoviesRepository
) : ViewModel() {

    val selectedMovie = MutableLiveData<MovieDetailsState>()
    private var moviesData = MutableLiveData<MovieListState>()

    fun getMoviesData(): LiveData<MovieListState> = moviesData

    fun onPopularMoviesListRequested() {
        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            val movies = repo.getPopularMovies()
            moviesData.value = if (movies.isEmpty()) {
                MovieListState.Empty
            } else {
                MovieListState.Data(movies)
            }
        }
    }

    fun onNowPlayingMoviesListRequested() {
        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            val movies = repo.getNowPlayingMovies()
            moviesData.value = if (movies.isEmpty()) {
                MovieListState.Empty
            } else {
                MovieListState.Data(movies)
            }
        }
    }

    fun onTopRatedMoviesListRequested() {
        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            val movies = repo.getTopRatedMovies()
            moviesData.value = if (movies.isEmpty()) {
                MovieListState.Empty
            } else {
                MovieListState.Data(movies)
            }
        }
    }

    fun onUpcomingMoviesListRequested() {
        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            val movies = repo.getUpcomingMovies()
            moviesData.value = if (movies.isEmpty()) {
                MovieListState.Empty
            } else {
                MovieListState.Data(movies)
            }
        }
    }

    fun onSearchMoviesRequested(query: String) {
        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            val movies = repo.searchMovies(query)
            moviesData.value = if (movies.isEmpty()) {
                MovieListState.Empty
            } else {
                MovieListState.Data(movies)
            }
        }
    }

    fun onMovieSelected(movie: Movie) {
        selectedMovie.value = MovieDetailsState.Loading
        viewModelScope.launch {
            val movieDetails = repo.getMovieDetails(movie)
            selectedMovie.value = MovieDetailsState.Data(movieDetails)
        }
    }

    fun isEmptyInstanceState() = !handle.contains("STATE_MOVIES")
    fun getIntStateParam(key: String): Int = handle.get(key) ?: 0
    fun getStringStateParam(key: String): String = handle.get(key) ?: ""
    fun getListStateParam(key: String): List<Movie> = handle.get(key) ?: emptyList()

    fun setInstanceStateParam(key: String, value: Any) {
        handle.set(key, value)
    }

    fun setListStateParam(key: String, value: List<Movie>?) {
        handle.set(key, value)
    }


    sealed class MovieListState {
        data class Data(val movies: List<Movie>) : MovieListState()
        object Empty : MovieListState()
        object Loading : MovieListState()
    }

    sealed class MovieDetailsState {
        data class Data(val movie: Movie) : MovieDetailsState()
        object Loading : MovieDetailsState()
    }
}