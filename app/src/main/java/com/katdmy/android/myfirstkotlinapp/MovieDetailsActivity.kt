package com.katdmy.android.myfirstkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MovieDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        val movie1 = findViewById<ImageView>(R.id.movie1)
        movie1.clipToOutline = true
        val movie2 = findViewById<ImageView>(R.id.movie2)
        movie2.clipToOutline = true
        val movie3 = findViewById<ImageView>(R.id.movie3)
        movie3.clipToOutline = true
        val movie4 = findViewById<ImageView>(R.id.movie4)
        movie4.clipToOutline = true
    }
}