package com.katdmy.android.myfirstkotlinapp.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.viewmodel.MoviesViewModel
import com.katdmy.android.myfirstkotlinapp.viewmodel.ViewModelFactory
import com.katdmy.android.myfirstkotlinapp.model.Movie

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MoviesViewModel by activityViewModels { ViewModelFactory(requireContext()) }

    private var loadingSpinner: ProgressBar? = null
    private var recycler: RecyclerView? = null
    private var adapter: MoviesAdapter? = null
    private var movieClickListener: MovieFragmentClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setUpAdapter()
        setUpClickListener()

        viewModel.onMoviesListRequested()
        viewModel.movies.observe(viewLifecycleOwner, this::updateAdapter)
    }

    override fun onDestroyView() {
        recycler?.adapter = null
        recycler = null
        loadingSpinner = null
        movieClickListener = null
        super.onDestroyView()
    }

    private fun initViews(view: View) {
        loadingSpinner = view.findViewById(R.id.loading_spinner)
        recycler = view.findViewById(R.id.movies_list)
    }

    private fun setUpAdapter() {
        recycler?.layoutManager = GridLayoutManager(activity, 2)
        adapter = MoviesAdapter {movie: Movie -> movieClickListener(movie)}
        recycler?.adapter = adapter
    }

    private fun setUpClickListener() {
        movieClickListener = context as? MovieFragmentClickListener
    }

    private fun updateAdapter(movies: List<Movie>) {
        adapter?.setData(movies)
        loadingSpinner?.visibility = View.GONE
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