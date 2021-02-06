package com.katdmy.android.myfirstkotlinapp.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.katdmy.android.myfirstkotlinapp.MovieApplication
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.viewmodel.MoviesViewModel
import com.katdmy.android.myfirstkotlinapp.viewmodel.ViewModelFactory
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.repository.MoviesPagedAdapter

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MoviesViewModel by activityViewModels { ViewModelFactory(requireActivity()) }

    private var searchTi: TextInputLayout? = null
    private var loadingSpinner: ProgressBar? = null
    private var emptyDataTv: TextView? = null
    private var tabLayout: TabLayout? = null
    private var recycler: RecyclerView? = null
    private var adapter: MoviesPagedAdapter? = null
    private var movieClickListener: MovieFragmentClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setUpAdapter()
        setUpClickListener()

        if (viewModel.isNotEmptyInstanceState()) {
            searchTi?.editText?.setText(viewModel.getStringStateParam("STATE_SEARCH"))

            val tabIndex = viewModel.getIntStateParam("STATE_TAB")
            val tab: TabLayout.Tab? = tabLayout?.getTabAt(tabIndex)
            tab?.select()

            //adapter?.setData(viewModel.getListStateParam("STATE_MOVIES"))
        } //else {
            //viewModel.getMoviesData().observe(viewLifecycleOwner, this::updateAdapter)
        //}
        viewModel.pagedMovies.observe(viewLifecycleOwner, { moviesPagedList -> adapter?.setList(moviesPagedList) } )

    }

    override fun onDestroyView() {
        viewModel.setInstanceStateParam("STATE_SEARCH", searchTi?.editText?.text.toString())
        viewModel.setInstanceStateParam("STATE_TAB", tabLayout?.selectedTabPosition ?: 0)
        //viewModel.setListStateParam("STATE_MOVIES", adapter?.getData())

        recycler?.adapter = null
        recycler = null
        tabLayout = null
        emptyDataTv = null
        loadingSpinner = null
        searchTi = null
        movieClickListener = null
        super.onDestroyView()
    }

    private fun initViews(view: View) {
        searchTi = view.findViewById(R.id.search_et)
        loadingSpinner = view.findViewById(R.id.loading_spinner)
        emptyDataTv = view.findViewById(R.id.empty_data_tv)
        tabLayout = view.findViewById(R.id.tabs)
        recycler = view.findViewById(R.id.movies_list)
    }

    private fun setUpAdapter() {
        recycler?.layoutManager = GridLayoutManager(activity, 2)
        adapter = MoviesPagedAdapter { movie: Movie -> movieClickListener(movie) }
        recycler?.adapter = adapter
    }

    private fun setUpClickListener() {
        searchTi?.setEndIconOnClickListener {
            val queryString = searchTi?.editText?.text.toString()
            if (queryString.isNotEmpty()) {
                viewModel.onMoviesListRequested("Search", queryString)
                //viewModel.getMoviesData().observe(viewLifecycleOwner, ::updateAdapter)
            }
        }
        movieClickListener = context as? MovieFragmentClickListener
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.onMoviesListRequested(tab?.text.toString())
                //viewModel.getMoviesData().observe(viewLifecycleOwner, ::updateAdapter)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.onMoviesListRequested(tab?.text.toString())
                //viewModel.getMoviesData().observe(viewLifecycleOwner, ::updateAdapter)
            }
        })
    }

/*    private fun updateAdapter(moviesData: MoviesViewModel.MovieListState) {
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
    }*/

    //creating method to make it look simpler
    private fun movieClickListener(movie: Movie) {
        viewModel.onMovieSelected(movie)
        movieClickListener?.showDetailView()
    }

    interface MovieFragmentClickListener {
        fun showDetailView()
    }
}