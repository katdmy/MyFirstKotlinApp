package com.katdmy.android.myfirstkotlinapp.viewmodel

import com.katdmy.android.myfirstkotlinapp.model.Actor
import com.katdmy.android.myfirstkotlinapp.model.Movie

sealed class MovieDetailsState {
    data class Data(val movie: Movie, val actors: List<Actor>) : MovieDetailsState()
    data class LoadingActors(val movie: Movie) : MovieDetailsState()
    data class EmptyActors(val movie: Movie) : MovieDetailsState()
}