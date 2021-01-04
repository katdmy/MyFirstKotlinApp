package com.katdmy.android.myfirstkotlinapp.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.viewmodel.MoviesViewModel
import com.katdmy.android.myfirstkotlinapp.viewmodel.ViewModelFactory
import com.katdmy.android.myfirstkotlinapp.model.Movie

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MoviesViewModel by activityViewModels { ViewModelFactory(requireContext()) }

    private var loadingSpinner: ProgressBar? = null
    private var emptyDataTv: TextView? = null
    private var tabLayout: TabLayout? = null
    private var recycler: RecyclerView? = null
    private var adapter: MoviesAdapter? = null
    private var movieClickListener: MovieFragmentClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setUpAdapter()
        setUpClickListener()

        viewModel.onPopularMoviesListRequested()
        viewModel.getMoviesData().observe(viewLifecycleOwner, this::updateAdapter)
    }

    override fun onDestroyView() {
        recycler?.adapter = null
        recycler = null
        tabLayout = null
        emptyDataTv = null
        loadingSpinner = null
        movieClickListener = null
        super.onDestroyView()
    }

    private fun initViews(view: View) {
        loadingSpinner = view.findViewById(R.id.loading_spinner)
        emptyDataTv = view.findViewById(R.id.empty_data_tv)
        tabLayout = view.findViewById(R.id.tabs)
        recycler = view.findViewById(R.id.movies_list)
    }

    private fun setUpAdapter() {
        recycler?.layoutManager = GridLayoutManager(activity, 2)
        adapter = MoviesAdapter {movie: Movie -> movieClickListener(movie)}
        recycler?.adapter = adapter
    }

    private fun setUpClickListener() {
        movieClickListener = context as? MovieFragmentClickListener
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    getString(R.string.tab_label_popular) -> viewModel.onPopularMoviesListRequested()
                    getString(R.string.tab_label_now_playing) -> viewModel.onNowPlayingMoviesListRequested()
                    getString(R.string.tab_label_top_rated) -> viewModel.onTopRatedMoviesListRequested()
                    getString(R.string.tab_label_upcoming) -> viewModel.onUpcomingMoviesListRequested()
                    else -> return
                }
                viewModel.getMoviesData().observe(viewLifecycleOwner, ::updateAdapter)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateAdapter(moviesData: MoviesViewModel.MovieListState) {
        when (moviesData) {
            is MoviesViewModel.MovieListState.Data -> {
                    adapter?.setData(moviesData.movies)
                    loadingSpinner?.visibility = View.GONE
                    emptyDataTv?.visibility = View.GONE
                }
            MoviesViewModel.MovieListState.Loading -> {
                    adapter?.setData(emptyList())
                    loadingSpinner?.visibility = View.VISIBLE
                    emptyDataTv?.visibility = View.GONE
                }
            MoviesViewModel.MovieListState.Empty -> {
                    adapter?.setData(emptyList())
                    loadingSpinner?.visibility = View.GONE
                    emptyDataTv?.visibility = View.VISIBLE
                }
        }
    }

    //creating method to make it look simpler
    private fun movieClickListener(movie: Movie) {
        viewModel.onMovieSelected(movie)
        movieClickListener?.showDetailView()
    }

    interface MovieFragmentClickListener {
        fun showDetailView()
    }
}