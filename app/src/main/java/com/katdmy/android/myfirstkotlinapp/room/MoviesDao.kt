package com.katdmy.android.myfirstkotlinapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies ")
    suspend fun getAllMovies(): List<MovieEntity>

    @Delete
    suspend fun deleteAllMovies()

    @Insert
    suspend fun insertMovie(movie: MovieEntity)
}