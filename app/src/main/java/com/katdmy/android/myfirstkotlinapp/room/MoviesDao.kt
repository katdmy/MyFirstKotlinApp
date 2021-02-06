package com.katdmy.android.myfirstkotlinapp.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.paging.MovieDataSourceFactory
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies ")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Insert
    suspend fun insertAllMovies(movies: List<MovieEntity>)

    @Transaction
    suspend fun replaceMovies(movies: List<MovieEntity>) {
        deleteAllMovies()
        insertAllMovies(movies)
    }


    @Query("SELECT * FROM actors")
    fun getAllActors(): Flow<List<ActorEntity>>

    @Query("DELETE FROM actors")
    suspend fun deleteAllActors()

    @Insert
    suspend fun insertAllActors(actors: List<ActorEntity>)

    @Transaction
    suspend fun replaceActors(actors: List<ActorEntity>) {
        deleteAllActors()
        insertAllActors(actors)
    }

/*    @Query("SELECT * FROM movies ")
    fun pagedMovies(): MovieDataSourceFactory*/
}