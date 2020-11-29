package com.katdmy.android.myfirstkotlinapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentMoviesDetails : Fragment() {
    private var listener: ClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val movie1 = view.findViewById<ImageView>(R.id.movie1)
        movie1.clipToOutline = true
        val movie2 = view.findViewById<ImageView>(R.id.movie2)
        movie2.clipToOutline = true
        val movie3 = view.findViewById<ImageView>(R.id.movie3)
        movie3.clipToOutline = true
        val movie4 = view.findViewById<ImageView>(R.id.movie4)
        movie4.clipToOutline = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = view.findViewById<TextView>(R.id.back).apply {
            setOnClickListener {  listener?.detailsBack() }
        }
    }

    fun setListener(l: ClickListener) {
        listener = l
    }

    interface ClickListener {
        fun detailsBack()
    }
}