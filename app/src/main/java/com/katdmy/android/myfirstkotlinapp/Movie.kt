package com.katdmy.android.myfirstkotlinapp

data class Movie(
    var image: Int,
    var pg: Int,
    val like: Boolean,
    var reviews: Int,
    var rating: Float,
    var tag: String,
    var name: String,
    var length: Int,
) {
}