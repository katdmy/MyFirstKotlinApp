package com.katdmy.android.myfirstkotlinapp.model

import com.katdmy.android.myfirstkotlinapp.room.ActorEntity
import com.katdmy.android.myfirstkotlinapp.room.MovieEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ModelsMapper {

    fun movieFromRoomToModel(movieEntity: MovieEntity): Movie {
        return Movie(
            id = movieEntity.id,
            title = movieEntity.title,
            overview = movieEntity.overview,
            poster = movieEntity.poster,
            backdrop = movieEntity.backdrop,
            ratings = movieEntity.ratings,
            numberOfRatings = movieEntity.numberOfRatings,
            minimumAge = movieEntity.minimumAge,
            runtime = movieEntity.runtime,
            genres = Json.decodeFromString(movieEntity.genres),
            actors = emptyList()
        )
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
            )
        }
    }

    fun actorFromRoomToModel(actorEntity: ActorEntity): Actor {
        return Actor(
            id = actorEntity.id,
            name = actorEntity.name,
            picture = actorEntity.picture
        )
    }

    fun actorsFromModelToRoom(actors: List<Actor>, movieId: Int): List<ActorEntity> {
        return actors.map {
            ActorEntity(
                id = it.id,
                name = it.name,
                picture = it.picture,
                movieId = movieId
            )
        }
    }
}