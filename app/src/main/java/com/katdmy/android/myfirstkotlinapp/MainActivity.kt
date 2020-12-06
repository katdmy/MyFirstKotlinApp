package com.katdmy.android.myfirstkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), FragmentMoviesList.MovieFragmentClickListener, FragmentMoviesDetails.BackClickListener {

    private val moviesList = FragmentMoviesList()
    private val moviesDetails = FragmentMoviesDetails()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout, moviesList)
            .commit()
    }

    override fun showDetailView(movie: Movie) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, FragmentMoviesDetails.newInstance(movie.name,
                movie.pg,
                movie.tag,
                movie.rating,
                movie.reviews))
                .commit()
        //moviesDetails.setMovieData(movie)
    }

    override fun detailsBack() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, moviesList)
            .commit()
    }


}