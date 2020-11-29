package com.katdmy.android.myfirstkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), FragmentMoviesList.ClickListener, FragmentMoviesDetails.ClickListener {

    private val moviesList = FragmentMoviesList().apply { setListener(this@MainActivity) }
    private val moviesDetails = FragmentMoviesDetails().apply { setListener(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .apply {
                add(R.id.frame_layout, moviesList)
                commit()
            }
    }

    override fun showDetailView() {
        supportFragmentManager.beginTransaction()
            .apply {
                add(R.id.frame_layout, moviesDetails)
                commit()
            }
    }

    override fun detailsBack() {
        supportFragmentManager.beginTransaction()
            .apply {
                remove(supportFragmentManager.fragments.get(supportFragmentManager.fragments.size-1))
                commit()
            }
    }
}