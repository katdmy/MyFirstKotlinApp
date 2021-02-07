package com.katdmy.android.myfirstkotlinapp.viewmodel

import com.katdmy.android.myfirstkotlinapp.model.Movie

sealed class MovieListState {
    data class Data(val movies: List<Movie>) : MovieListState()
    object Empty : MovieListState()
    object Loading : MovieListState()
}