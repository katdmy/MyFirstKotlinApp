package com.katdmy.android.myfirstkotlinapp.data

data class Genre(val id: Int, val name: String) {
    override fun toString(): String {
        return name
    }
}