package com.katdmy.android.myfirstkotlinapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoviesAdapter(private val listener: MovieClickListener): RecyclerView.Adapter<MoviesViewHolder>() {
    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_movie, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.onBind(movies[position])
        holder.itemView.setOnClickListener { listener.onClick(movies[position]) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

}

class MoviesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.movie_bg)
    private val pg: TextView = itemView.findViewById(R.id.pg)
    private val like: ImageView = itemView.findViewById(R.id.like)
    private val reviews: TextView = itemView.findViewById(R.id.reviews_list_name)
    private val rating: RatingBar = itemView.findViewById(R.id.rating)
    private val tag: TextView = itemView.findViewById(R.id.tag)
    private val name: TextView = itemView.findViewById(R.id.name_list)
    private val length: TextView = itemView.findViewById(R.id.length_list)

    fun onBind(movie: Movie) {
        image.setImageDrawable(itemView.context.getDrawable(movie.image))
        pg.text = "${movie.pg}+"
        like.setColorFilter(R.color.purple)
        reviews.text = "${movie.reviews} reviews"
        rating.rating = movie.rating
        tag.text = movie.tag
        name.text = movie.name
        length.text = "${movie.length} min"
    }
}

interface MovieClickListener {
    fun onClick(movie: Movie)
}