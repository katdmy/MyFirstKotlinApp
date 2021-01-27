package com.katdmy.android.myfirstkotlinapp.room

import android.provider.BaseColumns

object MoviesContract {

    const val DATABASE_NAME = "movies.db"

    object MovieEntry {

        const val TABLE_NAME = "movies"

        //COLUMNS:
        const val COLUMN_MOVIE_ID = BaseColumns._ID
        const val COLUMN_MOVIE_TITLE = "title"
        const val COLUMN_MOVIE_OVERVIEW = "overview"
        const val COLUMN_MOVIE_POSTER = "poster"
        const val COLUMN_MOVIE_BACKDROP = "backdrop"
        const val COLUMN_MOVIE_RATINGS = "ratings"
        const val COLUMN_MOVIE_NUMBER_OF_RATING = "numberOfRating"
        const val COLUMN_MOVIE_MINIMUM_AGE = "minimumAge"
        const val COLUMN_MOVIE_RUNTIME = "runtime"
        const val COLUMN_MOVIE_GENRES = "genres"

//        val genres: List<Genre>,
//        val actors: List<Actor>
    }

    object ActorEntry {

        const val TABLE_NAME = "actors"

        //COLUMNS:
        const val COLUMN_ACTOR_ID = BaseColumns._ID
        const val COLUMN_ACTOR_NAME = "name"
        const val COLUMN_ACTOR_PICTURE = "picture"
        const val COLUMN_ACTOR_MOVIE_ID = "movieId"
    }
}