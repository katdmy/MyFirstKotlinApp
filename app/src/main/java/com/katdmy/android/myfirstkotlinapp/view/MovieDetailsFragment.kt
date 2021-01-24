package com.katdmy.android.myfirstkotlinapp.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.katdmy.android.myfirstkotlinapp.MovieApplication
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.viewmodel.MoviesViewModel
import com.katdmy.android.myfirstkotlinapp.viewmodel.ViewModelFactory

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val viewModel: MoviesViewModel by activityViewModels {
        ViewModelFactory(requireActivity(), requireActivity().application as MovieApplication)
    }

    private var loadingSpinner: ProgressBar? = null
    private var emptyDataTv: TextView? = null
    private var scrollView: NestedScrollView? = null

    private var backListener: BackClickListener? = null
    private var recycler: RecyclerView? = null
    private var adapter: ActorsAdapter? = null

    private var backdrop: ImageView? = null
    private var backButton: TextView? = null
    private var nameTextView: TextView? = null
    private var pgTextView: TextView? = null
    private var tagTextView: TextView? = null
    private var ratingBar: RatingBar? = null
    private var reviewsTextView: TextView? = null
    private var overviewTextView: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setUpAdapter()
        setUpClickListener()

        viewModel.selectedMovie.observe(viewLifecycleOwner, this::onMovieDetailsStateChange)
    }

    override fun onDestroyView() {
        recycler?.adapter = null
        recycler = null
        backListener = null
        recycler = null
        backButton = null
        nameTextView = null
        pgTextView = null
        tagTextView = null
        ratingBar = null
        reviewsTextView = null
        overviewTextView = null
        backdrop = null
        loadingSpinner = null
        emptyDataTv = null
        scrollView = null
        super.onDestroyView()
    }


    private fun initViews(view: View) {
        recycler = view.findViewById(R.id.actors_list)
        backButton = view.findViewById(R.id.back)
        nameTextView = view.findViewById(R.id.name)
        pgTextView = view.findViewById(R.id.pg)
        tagTextView = view.findViewById(R.id.tag)
        ratingBar = view.findViewById(R.id.rating)
        reviewsTextView = view.findViewById(R.id.reviews_name)
        overviewTextView = view.findViewById(R.id.overview)
        backdrop = view.findViewById(R.id.orig)
        loadingSpinner = view.findViewById(R.id.loading_spinner)
        emptyDataTv = view.findViewById(R.id.empty_data_tv)
        scrollView = view.findViewById(R.id.scroll_view)
    }

    private fun setUpAdapter() {
        recycler?.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        adapter = ActorsAdapter { adapter?.shuffleData() }
        recycler?.adapter = adapter
    }

    private fun setUpClickListener() {
        backListener = context as? BackClickListener
        backButton?.setOnClickListener { backListener?.detailsBack() }
    }

    private fun onMovieDetailsStateChange(movieDetails: MoviesViewModel.MovieDetailsState) {
        when (movieDetails) {
            is MoviesViewModel.MovieDetailsState.Data -> {
                scrollView?.visibility = View.VISIBLE
                loadingSpinner?.visibility = View.GONE
                emptyDataTv?.visibility = View.GONE
                fillMovieDetails(movieDetails.movie)
            }
            MoviesViewModel.MovieDetailsState.Loading -> {
                scrollView?.visibility = View.GONE
                loadingSpinner?.visibility = View.VISIBLE
                emptyDataTv?.visibility = View.GONE
            }
        }
    }

    private fun fillMovieDetails(movie: Movie) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.movie_placeholder)
            .error(R.drawable.movie_placeholder)
        Glide.with(context)
            .load(movie.backdrop)
            .apply(options)
            .into(backdrop)

        nameTextView?.text = movie.title
        pgTextView?.text = "${movie.minimumAge}+"
        tagTextView?.text = movie.genres.toString().replace("[", "").replace("]", "")
        ratingBar?.rating = movie.ratings.div(2)
        reviewsTextView?.text = "${movie.numberOfRatings} reviews"
        overviewTextView?.text = movie.overview

        adapter?.setData(movie.actors)
    }

    interface BackClickListener {
        fun detailsBack()
    }
}