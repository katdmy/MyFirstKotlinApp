package com.katdmy.android.myfirstkotlinapp.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import kotlinx.coroutines.*

class MovieDataSource(
    private val repo: MoviesRepository,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Movie>() {

    private var supervisorJob = SupervisorJob()
    private val TAG = MovieDataSource::class.java.simpleName

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        Log.d(TAG, "loadInitial, requestedStartPosition = " + params.requestedLoadSize +
                ", requestedLoadSize = " + params.requestedLoadSize)
        executeQuery(1) { callback.onResult(it, null, 2) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val page = params.key
        executeQuery(page) { callback.onResult(it, page - 1) }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        Log.d(TAG, "loadAfter, key = " + params.key + ", loadSize = " + params.requestedLoadSize);
        val page = params.key
        executeQuery(page) { callback.onResult(it, page + 1) }
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()  // Cancel possible running job to only keep last result searched by user
    }

    private fun executeQuery(
        page: Int,
        callback: (List<Movie>) -> Unit
    ) {
        scope.launch(getJobErrorHandler() + supervisorJob) {
            delay(200) // To handle user typing case
            val movies = repo.getPagedMovies(page)
            callback(movies)
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(TAG, "Error: $e")
        //networkState.postValue(NetworkState.FAILED)
    }
}