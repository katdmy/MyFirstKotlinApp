package com.katdmy.android.myfirstkotlinapp.repository

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.view.MoviesViewHolder

class MoviesPagedAdapter(private val movieOnClickListener: (Movie) -> Unit) : PagedListAdapter<Movie, MoviesViewHolder>(DIFF_CALLBACK) {

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

    fun setList(list: PagedList<Movie>?) {
        submitList(list)
    }

}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.equals(newItem)
    }
}