package com.katdmy.android.myfirstkotlinapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.model.Movie

class MoviesAdapter(private val movieOnClickListener: (Movie) -> Unit) :
    RecyclerView.Adapter<MoviesViewHolder>() {
    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_movie, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.onBind(movies[position])
        holder.itemView.setOnClickListener { movieOnClickListener(movies[position]) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    fun getData() = movies
}