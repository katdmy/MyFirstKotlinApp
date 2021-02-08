package com.katdmy.android.myfirstkotlinapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = MoviesContract.ActorEntry.TABLE_NAME,
    indices = [Index(MoviesContract.ActorEntry.COLUMN_ACTOR_ID)]
)
data class ActorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MoviesContract.ActorEntry.COLUMN_ACTOR_ID)
    val id: Int = 0,
    @ColumnInfo(name = MoviesContract.ActorEntry.COLUMN_ACTOR_NAME)
    val name: String,
    @ColumnInfo(name = MoviesContract.ActorEntry.COLUMN_ACTOR_PICTURE)
    val picture: String,
    @ColumnInfo(name = MoviesContract.ActorEntry.COLUMN_ACTOR_MOVIE_ID)
    val movieId: Int
)