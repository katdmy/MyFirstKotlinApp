package com.katdmy.android.myfirstkotlinapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    var poster: String,
    var backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    var runtime: Int,
    val genres: List<Genre>,
    var actors: List<Actor>
) : Parcelable