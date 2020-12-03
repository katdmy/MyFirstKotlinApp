package com.katdmy.android.myfirstkotlinapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        view.findViewById<View>(R.id.movie_list_item).setOnClickListener {  listener?.showDetailView() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? ClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface ClickListener {
        fun showDetailView()
    }
}