package com.katdmy.android.myfirstkotlinapp.room

import android.provider.BaseColumns

object MoviesContract {

    const val DATABASE_NAME = "movies.db"

    object MovieEntry {

        const val TABLE_NAME = "movies"

        //COLUMNS:
        const val COLUMN_MOVIE_ID = BaseColumns._ID
        const val COLUMN_MOVIE_TITLE = "title"
        const val COLUMN_MOVIE_OVERVIEW = "latitude"
        const val COLUMN_MOVIE_POSTER = "longitude"
        const val COLUMN_MOVIE_BACKDROP = "backdrop"
        const val COLUMN_MOVIE_RATINGS = "ratings"
        const val COLUMN_MOVIE_NUMBER_OF_RATING = "numberOfRating"
        const val COLUMN_MOVIE_MINIMUM_AGE = "minimumAge"
        const val COLUMN_MOVIE_RUNTIME = "runtime"

//        val genres: List<Genre>,
//        val actors: List<Actor>
    }
}