package com.katdmy.android.myfirstkotlinapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.katdmy.android.myfirstkotlinapp.data.Movie
import com.katdmy.android.myfirstkotlinapp.data.loadMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesRepository(
        private val context: Context,
        private val dispatcher: CoroutineDispatcher
) {

    suspend fun getMovies(): List<Movie> =
            withContext(dispatcher) { loadMovies(context) }
}