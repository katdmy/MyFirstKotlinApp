package com.katdmy.android.myfirstkotlinapp

import android.app.Application
import com.katdmy.android.myfirstkotlinapp.repository.MoviesRepository
import com.katdmy.android.myfirstkotlinapp.retrofit.RetrofitClient
import com.katdmy.android.myfirstkotlinapp.room.MoviesDb

class MovieApplication : Application() {

    val db by lazy { MoviesDb.create(this) }
}