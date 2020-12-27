package com.katdmy.android.myfirstkotlinapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
            MoviesViewModel::class.java -> MoviesViewModel(MoviesRepository(context = context, dispatcher = Dispatchers.IO))
            else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
        } as T
}