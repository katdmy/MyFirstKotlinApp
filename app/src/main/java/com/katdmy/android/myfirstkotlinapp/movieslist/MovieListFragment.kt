package com.katdmy.android.myfirstkotlinapp.movieslist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.MoviesViewModel
import com.katdmy.android.myfirstkotlinapp.ViewModelFactory
import com.katdmy.android.myfirstkotlinapp.data.Movie

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MoviesViewModel by activityViewModels { ViewModelFactory(requireContext()) }

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
        movieClickListener = null
        super.onDestroyView()
    }

    private fun initViews(view: View) {
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