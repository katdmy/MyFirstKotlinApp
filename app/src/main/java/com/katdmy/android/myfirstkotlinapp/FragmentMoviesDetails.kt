package com.katdmy.android.myfirstkotlinapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.katdmy.android.myfirstkotlinapp.data.Movie
import com.katdmy.android.myfirstkotlinapp.data.loadMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMoviesDetails : Fragment() {
    private val scope = CoroutineScope(Dispatchers.Main)

    private var backListener: BackClickListener? = null
    private var recycler: RecyclerView? = null
    var adapter: ActorsAdapter? = null

    private var movieData: Movie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { movieData = it.getParcelable<Movie>(PARAM_MOVIE) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.back).setOnClickListener { backListener?.detailsBack() }

        val backdrop = view.findViewById<ImageView>(R.id.orig)
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.movie_placeholder)
            .error(R.drawable.movie_placeholder)
        Glide.with(context)
            .load(movieData?.backdrop)
            .apply(options)
            .into(backdrop)

        view.findViewById<TextView>(R.id.name).text = movieData?.title
        view.findViewById<TextView>(R.id.pg).text = "${movieData?.minimumAge}+"
        view.findViewById<TextView>(R.id.tag).text = movieData?.genres.toString().replace("[", "").replace("]", "");
        view.findViewById<RatingBar>(R.id.rating).rating = movieData?.ratings?.div(2) ?: 0f
        view.findViewById<TextView>(R.id.reviews_name).text = "${movieData?.numberOfRatings} reviews"
        view.findViewById<TextView>(R.id.overview).text = movieData?.overview

        recycler = view.findViewById(R.id.actors_list)
        recycler?.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        adapter = ActorsAdapter(clickListener)
        recycler?.adapter = adapter

        //scope.launch { adapter?.setData(loadActors(view.context)) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backListener = context as? BackClickListener
    }

    override fun onDetach() {
        super.onDetach()
        recycler = null
        backListener = null
    }

    private var clickListener = object : ActorsClickListener {
        override fun onClick() {
            adapter?.shuffleData()
        }
    }

    interface BackClickListener {
        fun detailsBack()
    }

    companion object {
        private const val PARAM_MOVIE = "movie"

        fun newInstance(
            movie: Movie
        ): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            val args = Bundle()
            args.putParcelable(PARAM_MOVIE, movie)
            fragment.arguments = args
            return fragment
        }
    }
}