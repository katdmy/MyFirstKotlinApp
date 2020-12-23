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
import com.katdmy.android.myfirstkotlinapp.data.Movie
import com.katdmy.android.myfirstkotlinapp.data.loadMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Thread.sleep

class FragmentMoviesList : Fragment() {

    private val scope = CoroutineScope(Dispatchers.Main)

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

        scope.launch { adapter?.setData(loadMovies(view.context)) }
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