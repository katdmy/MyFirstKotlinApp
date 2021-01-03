package com.katdmy.android.myfirstkotlinapp.viewmodel

import androidx.lifecycle.*
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel() : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()
    val selected = MutableLiveData<Movie>()

    fun onMoviesListRequested() {
        viewModelScope.launch {
            movies.value = MoviesRepository.getPopularMovies()
        }
    }

    fun onMovieSelected(movie: Movie) {
        viewModelScope.launch {
            selected.value = MoviesRepository.getMovieDetails(movie)
        }
    }
}