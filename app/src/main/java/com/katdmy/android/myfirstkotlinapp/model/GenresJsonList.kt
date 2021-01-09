package com.katdmy.android.myfirstkotlinapp.model

import kotlinx.serialization.Serializable

@Serializable
data class GenresJsonList(
	val genres: List<GenresItem>? = null
)

@Serializable
data class GenresItem(
	val name: String? = null,
	val id: Int? = null
)
