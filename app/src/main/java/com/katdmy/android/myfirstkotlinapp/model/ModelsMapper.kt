package com.katdmy.android.myfirstkotlinapp.model

import com.katdmy.android.myfirstkotlinapp.room.ActorEntity
import com.katdmy.android.myfirstkotlinapp.room.MovieEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ModelsMapper {

    fun moviesFromRoomToModel(movieEntities: List<MovieEntity>): List<Movie> {
        return movieEntities.map {
            Movie(
                id = it.id,
                title = it.title,
                overview = it.overview,
                poster = it.poster,
                backdrop = it.backdrop,
                ratings = it.ratings,
                numberOfRatings = it.numberOfRatings,
                minimumAge = it.minimumAge,
                runtime = it.runtime,
                genres = Json.decodeFromString(it.genres),
                actors = emptyList()
            )
        }
    }

    fun moviesFromModelToRoom(movies: List<Movie>): List<MovieEntity> {
        return movies.map {
            MovieEntity(
                id = it.id,
                title = it.title,
                overview = it.overview,
                poster = it.poster,
                backdrop = it.backdrop,
                ratings = it.ratings,
                numberOfRatings = it.numberOfRatings,
                minimumAge = it.minimumAge,
                runtime = it.runtime,
                genres = Json.encodeToString(it.genres)
                //it.genres.joinToString(prefix = "[", postfix = "]")
            )
        }
    }

    fun actorsFromRoomToModel(movieEntities: List<ActorEntity>): List<Actor> {
        return movieEntities.map {
            Actor(
                id = it.id,
                name = it.name,
                picture = it.picture
            )
        }
    }

    fun actorsFromModelToRoom(movies: List<Actor>): List<ActorEntity> {
        return movies.map {
            ActorEntity(
                id = it.id,
                name = it.name,
                picture = it.picture
            )
        }
    }
}