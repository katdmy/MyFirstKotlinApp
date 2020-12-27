package com.katdmy.android.myfirstkotlinapp

import androidx.lifecycle.*
import com.katdmy.android.myfirstkotlinapp.data.Movie
import kotlinx.coroutines.launch

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()
    val selected = MutableLiveData<Movie>()

    fun onMoviesListRequested() {
        viewModelScope.launch {
            movies.value = repository.getMovies()
        }
    }

    fun onMovieSelected(movie: Movie) {
        selected.value = movie
    }
}