package com.katdmy.android.myfirstkotlinapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorsJsonList(
	val cast: List<CastItem>? = null,
	val id: Int? = null,
)

@Serializable
data class CastItem(
	@SerialName("cast_id")
	val castId: Int? = null,
	val character: String? = null,
	val gender: Int? = null,
	@SerialName("credit_id")
	val creditId: String? = null,
	@SerialName("known_for_department")
	val knownForDepartment: String? = null,
	@SerialName("original_name")
	val originalName: String? = null,
	val popularity: Double? = null,
	@SerialName("name")
	val name: String? = null,
	@SerialName("profile_path")
	val profilePath: String? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	val order: Int? = null
)
