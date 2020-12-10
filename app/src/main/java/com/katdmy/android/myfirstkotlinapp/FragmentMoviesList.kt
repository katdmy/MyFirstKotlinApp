package com.katdmy.android.myfirstkotlinapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class FragmentMoviesList : Fragment() {
    private var recycler: RecyclerView? = null
    var adapter: MoviesAdapter? = null
    private var movieClickListener: MovieFragmentClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_movie_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.movies_list)
        recycler?.layoutManager = GridLayoutManager(activity, 2)
        adapter = MoviesAdapter(clickListener)
        recycler?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        adapter?.setData(getMovies())
    }

    private fun getMovies(): List<Movie> {
        val movies = mutableListOf<Movie>()
        movies.add(Movie(R.drawable.movie_bg1, 13, false, 125, 4.0f, "Action, Adventure, Drama", "Avengers: End Game", 137))
        movies.add(Movie(R.drawable.movie_bg2, 16, true, 98, 5.0f, "Action, Sci-Fi, Thriller", "Tenet", 97))
        movies.add(Movie(R.drawable.movie_bg3, 13, false, 38, 4.0f, "Action, Adventure, Sci-Fi", "Black Widow", 102))
        movies.add(Movie(R.drawable.movie_bg4, 13, false, 74, 5.0f, "Action, Adventure, Fantasy", "Wonder woman 1984", 120))
        return movies
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        movieClickListener = context as? MovieFragmentClickListener
    }

    override fun onDetach() {
        super.onDetach()
        recycler = null
        movieClickListener = null
    }

    private var clickListener = object : MoviesAdapterClickListener {
        override fun onClick(movie: Movie) {
            movieClickListener?.showDetailView(movie)
        }
    }

    interface MovieFragmentClickListener {
        fun showDetailView(movie: Movie)
    }

}