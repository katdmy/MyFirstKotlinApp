package com.katdmy.android.myfirstkotlinapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.paging.MovieDataSourceFactory
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MoviesViewModel(
    private val handle: SavedStateHandle,
    private val repo: MoviesRepository
) : ViewModel() {

    val selectedMovie = MutableLiveData<MovieDetailsState>()
    private var moviesData = MutableLiveData<MovieListState>()

    init {
        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            repo.observeMovieList().collect { moviesList ->
                moviesData.value = if (moviesList.isNotEmpty()) {
                    MovieListState.Data(moviesList)
                } else {
                    MovieListState.Empty
                }
            }
        }
        viewModelScope.launch { repo.loadMovies("Popular", "") }
    }

    fun getMoviesData(): LiveData<MovieListState> = moviesData

    fun onMoviesListRequested(
        mode: String,
        searchString: String = ""
    ) {
//        moviesData.value = MovieListState.Loading
        viewModelScope.launch {
            val movies = repo.loadMovies(mode, searchString)
            moviesData.value = if (movies.isEmpty()) {
                MovieListState.Empty
            } else {
                MovieListState.Data(movies)
            }
        }
    }

    fun onMovieSelected(movie: Movie) {
        selectedMovie.value = MovieDetailsState.Loading
        viewModelScope.launch {
            val movieDetails = repo.getMovieDetails(movie)
            selectedMovie.value = MovieDetailsState.Data(movieDetails)
        }
    }

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


    sealed class MovieListState {
        data class Data(val movies: List<Movie>) : MovieListState()
        object Empty : MovieListState()
        object Loading : MovieListState()
    }

    sealed class MovieDetailsState {
        data class Data(val movie: Movie) : MovieDetailsState()
        object Loading : MovieDetailsState()
    }


    // TODO: Paging
    private val movieDataSource = MovieDataSourceFactory(repo = repo, scope = viewModelScope)

    val pagedMovies: LiveData<PagedList<Movie>> = LivePagedListBuilder(movieDataSource, pagedListConfig()).build()

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(4)
        .setEnablePlaceholders(true)
        .setPageSize(20)
        .build()


    /*        val moviePagingConfig = Config (
            pageSize = 4,
            prefetchDistance = 16,
            enablePlaceholders = true
            )
    val moviesDataSource: DataSource.Factory<Int, Movie> = repo.getPagedMovies()

    val ioExecutor = Executors.newSingleThreadExecutor()

    val moviesList = moviesDataSource.toLiveData(
        pagingConfig = moviePagingConfig,
        executor = ioExecutor
    )*/

}

