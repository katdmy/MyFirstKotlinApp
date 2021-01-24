package com.katdmy.android.myfirstkotlinapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDb : RoomDatabase() {

    abstract val moviesDao: MoviesDao

    companion object {

        fun create(applicationContext: Context): MoviesDb = Room.databaseBuilder(
            applicationContext,
            MoviesDb::class.java,
            MoviesContract.DATABASE_NAME

        )
            .fallbackToDestructiveMigration()
            .build()

    }
}