package com.katdmy.android.myfirstkotlinapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.WorkManager
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.worker.WorkRepository

class MainActivity : AppCompatActivity(), MovieListFragment.MovieFragmentClickListener,
    MovieDetailsFragment.BackClickListener {

    private val TAG = MainActivity::class.java.simpleName

    private val movieList = MovieListFragment()
    private val movieDetails = MovieDetailsFragment()
    private val workRepository = WorkRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val action: String? = intent?.action
        val movieId = intent.data?.lastPathSegment?.toIntOrNull()
        Log.e(TAG, "movieId = ${movieId.toString()}")

        if (savedInstanceState == null) {
            if (movieId == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.frame_layout, movieList)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, MovieDetailsFragment.newInstance(movieId))
                    .commit()
            }
        }

        WorkManager.getInstance(application).enqueue(workRepository.periodDbUpdate)
    }

    override fun showDetailView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, movieDetails)
            .commit()
    }

    override fun detailsBack() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, movieList)
            .commit()
    }
}