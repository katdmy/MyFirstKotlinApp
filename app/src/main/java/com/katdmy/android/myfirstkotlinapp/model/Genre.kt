package com.katdmy.android.myfirstkotlinapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Genre(val id: Int, val name: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}