package com.katdmy.android.myfirstkotlinapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies ")
    fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): MovieEntity

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Insert
    suspend fun insertAllMovies(movies: List<MovieEntity>)

    @Transaction
    suspend fun replaceMovies(movies: List<MovieEntity>) {
        deleteAllMovies()
        insertAllMovies(movies)
    }


    @Query("SELECT * FROM actors WHERE movie_id = :movieId")
    fun getAllActors(movieId: Int): List<ActorEntity>

    @Query("DELETE FROM actors")
    suspend fun deleteAllActors()

    @Insert
    suspend fun insertAllActors(actors: List<ActorEntity>)

    @Transaction
    suspend fun replaceActors(actors: List<ActorEntity>) {
        deleteAllActors()
        insertAllActors(actors)
    }
}