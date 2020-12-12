package com.katdmy.android.myfirstkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.katdmy.android.myfirstkotlinapp.data.Movie

class MainActivity : AppCompatActivity(), FragmentMoviesList.MovieFragmentClickListener, FragmentMoviesDetails.BackClickListener {

    private val moviesList = FragmentMoviesList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.frame_layout, moviesList)
                    .commit()
        }
    }

    override fun showDetailView(movie: Movie) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, FragmentMoviesDetails.newInstance(movie))
                .commit()
    }

    override fun detailsBack() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, moviesList)
            .commit()
    }
}