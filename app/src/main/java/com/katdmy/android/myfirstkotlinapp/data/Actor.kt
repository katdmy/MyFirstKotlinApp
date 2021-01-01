package com.katdmy.android.myfirstkotlinapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Actor(
    val id: Int,
    val name: String,
    val picture: String
) : Parcelable