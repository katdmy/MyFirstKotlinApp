package com.katdmy.android.myfirstkotlinapp.viewmodel

import androidx.lifecycle.*
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel() : ViewModel() {

    private var moviesData = MutableLiveData<MovieListState>()
    val selectedMovie = MutableLiveData<MovieDetailsState>()

    fun getMoviesData() : LiveData<MovieListState> = moviesData

    fun onPopularMoviesListRequested() {
        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            val movies = MoviesRepository.getPopularMovies()
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
            val movies = MoviesRepository.getNowPlayingMovies()
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
            val movies = MoviesRepository.getTopRatedMovies()
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
            val movies = MoviesRepository.getUpcomingMovies()
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
            val movies = MoviesRepository.searchMovies(query)
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
            val movieDetails = MoviesRepository.getMovieDetails(movie)
            selectedMovie.value = MovieDetailsState.Data(movieDetails)
        }
    }

    sealed class MovieListState {
        data class Data(val movies: List<Movie>): MovieListState()
        object Empty: MovieListState()
        object Loading: MovieListState()
    }

    sealed class MovieDetailsState {
        data class Data(val movie: Movie): MovieDetailsState()
        object Loading: MovieDetailsState()
    }
}