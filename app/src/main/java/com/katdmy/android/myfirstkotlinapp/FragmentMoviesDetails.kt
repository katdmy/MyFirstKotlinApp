package com.katdmy.android.myfirstkotlinapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentMoviesDetails : Fragment() {
    private var backListener: BackClickListener? = null
    private var recycler: RecyclerView? = null
    var adapter: ActorsAdapter? = null

    private var movieName: String = ""
    private var moviePg: Int = 0
    private var movieTag: String = ""
    private var movieRating: Float = 0f
    private var movieReviews: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieName = it.getString(PARAM_NAME, "movie name")
            moviePg = it.getInt(PARAM_PG)
            movieTag = it.getString(PARAM_TAG, "tags")
            movieRating = it.getFloat(PARAM_RATING)
            movieReviews = it.getInt(PARAM_REVIEWS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.back).setOnClickListener { backListener?.detailsBack() }

        view.findViewById<TextView>(R.id.name).text = movieName
        view.findViewById<TextView>(R.id.pg).text = "${moviePg}+"
        view.findViewById<TextView>(R.id.tag).text = movieTag
        view.findViewById<RatingBar>(R.id.rating).rating = movieRating
        view.findViewById<TextView>(R.id.reviews_name).text = "${movieReviews} reviews"

        recycler = view.findViewById(R.id.actors_list)
        recycler?.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        adapter = ActorsAdapter(clickListener)
        recycler?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        adapter?.setData(getActors())
    }

    private fun getActors(): List<Actor> {
        val actors = mutableListOf<Actor>()
        actors.add(Actor(R.drawable.actor1, "Robert\nDowney Jr."))
        actors.add(Actor(R.drawable.actor2, "Chris\nEvans"))
        actors.add(Actor(R.drawable.actor3, "Mark\nRuffalo"))
        actors.add(Actor(R.drawable.actor4, "Chris Hemsworth"))
        return actors
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
        private const val PARAM_NAME = "movie_name"
        private const val PARAM_PG = "movie_pg"
        private const val PARAM_TAG = "movie_tag"
        private const val PARAM_RATING = "movie_rating"
        private const val PARAM_REVIEWS = "movie_reviews"

        fun newInstance(
                name: String,
                pg: Int,
                tag: String,
                rating: Float,
                reviews: Int
        ): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            val args = Bundle()
            args.putString(PARAM_NAME, name)
            args.putInt(PARAM_PG, pg)
            args.putString(PARAM_TAG, tag)
            args.putFloat(PARAM_RATING, rating)
            args.putInt(PARAM_REVIEWS, reviews)
            fragment.arguments = args
            return fragment
        }
    }
}