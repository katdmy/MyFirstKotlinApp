package com.katdmy.android.myfirstkotlinapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
            MoviesViewModel::class.java -> MoviesViewModel()
            else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
        } as T
}