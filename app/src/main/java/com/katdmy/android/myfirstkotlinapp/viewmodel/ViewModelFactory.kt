package com.katdmy.android.myfirstkotlinapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.katdmy.android.myfirstkotlinapp.MovieApplication
import com.katdmy.android.myfirstkotlinapp.model.ModelsMapper
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import com.katdmy.android.myfirstkotlinapp.retrofit.RetrofitClient.tmdbApi

class ViewModelFactory(
    private val activity: FragmentActivity,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(activity, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel(
            handle,
            MoviesRepository(tmdbApi, (activity.application as MovieApplication).db.moviesDao, ModelsMapper())
        )
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}