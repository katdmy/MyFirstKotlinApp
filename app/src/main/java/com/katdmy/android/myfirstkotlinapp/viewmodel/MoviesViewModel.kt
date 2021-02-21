package com.katdmy.android.myfirstkotlinapp.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository

class MoviesViewModel(
    private val handle: SavedStateHandle,
    private val repo: MoviesRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    lateinit var selectedMovie: Movie
    var moviesData: LiveData<MovieListState>

    init {
        val editor = sharedPreferences.edit()
        editor.putString("mode", "Popular")
        editor.putString("searchString", "")
        editor.apply()
        moviesData = repo.loadMovies("Popular", "")
    }

    fun onMoviesListRequested(
        mode: String,
        searchString: String = ""
    ) {
        val editor = sharedPreferences.edit()
        editor.putString("mode", mode)
        editor.putString("searchString", searchString)
        editor.apply()
        moviesData = repo.loadMovies(mode, searchString)
    }

    fun onMovieSelected() = repo.getMovieDetails(selectedMovie)

    fun getMovieById(movieId: Int) = repo.getMovieById(movieId)

    fun isNotEmptyInstanceState() = handle.contains("STATE_MOVIES")
    fun getIntStateParam(key: String): Int = handle.get(key) ?: 0
    fun getStringStateParam(key: String): String = handle.get(key) ?: ""
    fun getListStateParam(key: String): List<Movie> = handle.get(key) ?: emptyList()

    fun setInstanceStateParam(key: String, value: Any) {
        handle.set(key, value)
    }

    fun setListStateParam(key: String, value: List<Movie>?) {
        handle.set(key, value)
    }
}

