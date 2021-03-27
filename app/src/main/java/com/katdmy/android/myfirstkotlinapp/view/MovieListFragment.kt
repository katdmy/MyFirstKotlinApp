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
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialElevationScale
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.viewmodel.MoviesViewModel
import com.katdmy.android.myfirstkotlinapp.viewmodel.ViewModelFactory
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.viewmodel.MovieListState

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MoviesViewModel by activityViewModels { ViewModelFactory(requireActivity()) }

    private var queryString: String = ""

    private var searchTi: TextInputLayout? = null
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

        if (viewModel.isNotEmptyInstanceState()) {
            postponeEnterTransition()

            searchTi?.editText?.setText(viewModel.getStringStateParam("STATE_SEARCH"))

            val tabIndex = viewModel.getIntStateParam("STATE_TAB")
            val tab: TabLayout.Tab? = tabLayout?.getTabAt(tabIndex)
            tab?.select()

            adapter?.setData(viewModel.getListStateParam("STATE_MOVIES"))
        } else {
            viewModel.moviesData.observe(viewLifecycleOwner, this::updateAdapter)
        }
    }

    override fun onDestroyView() {
        viewModel.setInstanceStateParam("STATE_SEARCH", searchTi?.editText?.text.toString())
        viewModel.setInstanceStateParam("STATE_TAB", tabLayout?.selectedTabPosition ?: 0)
        viewModel.setListStateParam("STATE_MOVIES", adapter?.getData())

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
        adapter = MoviesAdapter { sharedView: View, movie: Movie ->
            movieClickListener(
                sharedView,
                movie
            )
        }
        recycler?.adapter = adapter
    }

    private fun setUpClickListener() {
        searchTi?.setEndIconOnClickListener {
            queryString = searchTi?.editText?.text.toString()
            if (queryString.isNotEmpty()) {
                viewModel.onMoviesListRequested("Search", queryString)
                viewModel.moviesData.observe(viewLifecycleOwner, ::updateAdapter)
            }
        }
        movieClickListener = context as? MovieFragmentClickListener
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                queryString = ""
                viewModel.onMoviesListRequested(tab?.text.toString(), queryString)
                viewModel.moviesData.observe(viewLifecycleOwner, ::updateAdapter)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val mode = if (queryString.isEmpty()) tab?.text.toString() else "Search"
                viewModel.onMoviesListRequested(mode, queryString)
                viewModel.moviesData.observe(viewLifecycleOwner, ::updateAdapter)
            }
        })
    }

    private fun updateAdapter(moviesData: MovieListState) {
        when (moviesData) {
            is MovieListState.Data -> {
                adapter?.setData(moviesData.movies)
                loadingSpinner?.visibility = View.GONE
                emptyDataTv?.visibility = View.GONE
                startPostponedEnterTransition()
            }
            MovieListState.Loading -> {
                adapter?.setData(emptyList())
                loadingSpinner?.visibility = View.VISIBLE
                emptyDataTv?.visibility = View.GONE
            }
            MovieListState.Empty -> {
                adapter?.setData(emptyList())
                loadingSpinner?.visibility = View.GONE
                emptyDataTv?.visibility = View.VISIBLE
            }
        }
    }

    //creating method to make it look simpler
    private fun movieClickListener(sharedView: View, movie: Movie) {
        viewModel.selectedMovie = movie
        exitTransition = MaterialElevationScale(false).apply {
            excludeTarget(sharedView, true)
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            excludeTarget(sharedView, true)
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        movieClickListener?.showDetailView(sharedView)
    }

    interface MovieFragmentClickListener {
        fun showDetailView(sharedView: View)
    }
}