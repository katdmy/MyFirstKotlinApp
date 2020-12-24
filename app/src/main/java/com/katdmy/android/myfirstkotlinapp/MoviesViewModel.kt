package com.katdmy.android.myfirstkotlinapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.katdmy.android.myfirstkotlinapp.data.Movie

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val model = MoviesModel(application)
    val movies: LiveData<List<Movie>> get() = model.getMovies()
    val selected = MutableLiveData<Movie>()

    fun select(movie: Movie) {
        Log.e("MovieViewModel", "Selected movie was ${selected.value}")
        selected.value = movie
        Log.e("MovieViewModel", "Selected movie is ${selected.value}")
    }
}