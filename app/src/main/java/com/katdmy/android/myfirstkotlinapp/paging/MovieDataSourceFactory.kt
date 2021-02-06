package com.katdmy.android.myfirstkotlinapp.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import com.katdmy.android.myfirstkotlinapp.room.MovieEntity
import kotlinx.coroutines.CoroutineScope

class MovieDataSourceFactory(
    private val repo: MoviesRepository,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val dataSource = MovieDataSource(repo, scope)
        moviesLiveDataSource.postValue(dataSource)
        return dataSource
    }
}

