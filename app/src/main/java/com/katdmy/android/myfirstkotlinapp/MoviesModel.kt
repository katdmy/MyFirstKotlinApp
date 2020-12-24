package com.katdmy.android.myfirstkotlinapp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katdmy.android.myfirstkotlinapp.data.Movie
import com.katdmy.android.myfirstkotlinapp.data.loadMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesModel(private val application: Application) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val movies = MutableLiveData<List<Movie>>(emptyList())

    fun getMovies(): LiveData<List<Movie>> {
        loadMoviesFromJson()
        return movies
    }

    private fun loadMoviesFromJson() {
        scope.launch { movies.postValue(loadMovies(application)) }
    }
}