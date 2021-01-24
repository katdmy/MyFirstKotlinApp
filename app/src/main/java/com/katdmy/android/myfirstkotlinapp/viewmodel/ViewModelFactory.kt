package com.katdmy.android.myfirstkotlinapp.viewmodel

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.katdmy.android.myfirstkotlinapp.MovieApplication
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import com.katdmy.android.myfirstkotlinapp.retrofit.RetrofitClient.tmdbApi

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val application: MovieApplication,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel(
            handle,
            MoviesRepository(tmdbApi, application.db.moviesDao)
        )
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}