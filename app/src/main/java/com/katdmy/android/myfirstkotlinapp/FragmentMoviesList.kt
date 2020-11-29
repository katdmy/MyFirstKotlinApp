package com.katdmy.android.myfirstkotlinapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class FragmentMoviesList : Fragment() {
    private var listener: ClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_movie_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moviesListItem = view.findViewById<View>(R.id.movie_list_item).apply {
            setOnClickListener {  listener?.showDetailView() }
        }
    }

    fun setListener(l: ClickListener) {
        listener = l
    }

    interface ClickListener {
        fun showDetailView()
    }
}