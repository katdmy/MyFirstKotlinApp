package com.katdmy.android.myfirstkotlinapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = MoviesContract.MovieEntry.TABLE_NAME,
    indices = [Index(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)]
)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_ROOM_ID)
    val roomId: Int = 0,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_ID)
    val id: Int = 0,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE)
    val title: String,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)
    val overview: String,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER)
    val poster: String,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_BACKDROP)
    val backdrop: String,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_RATINGS)
    val ratings: Float,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_NUMBER_OF_RATING)
    val numberOfRatings: Int,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_MINIMUM_AGE)
    val minimumAge: Int,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_RUNTIME)
    val runtime: Int,
    @ColumnInfo(name = MoviesContract.MovieEntry.COLUMN_MOVIE_GENRES)
    val genres: String
)