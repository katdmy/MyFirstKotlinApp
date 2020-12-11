package com.katdmy.android.myfirstkotlinapp.data

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    val runtime: Int,
    val genres: List<Genre>,
    val actors: List<Actor>
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        listOf<Genre>().apply { parcel.readList(this, Genre::class.java.classLoader) },
        listOf<Actor>().apply { parcel.readList(this, Actor::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(title)
        dest?.writeString(overview)
        dest?.writeString(poster)
        dest?.writeString(backdrop)
        dest?.writeFloat(ratings)
        dest?.writeInt(numberOfRatings)
        dest?.writeInt(minimumAge)
        dest?.writeInt(runtime)
        dest?.writeList(genres)
        dest?.writeList(actors)
    }
}