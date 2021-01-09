package com.katdmy.android.myfirstkotlinapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesJsonList(
	val page: Int? = null,
	@SerialName("total_pages")
	val totalPages: Int? = null,
	val results: List<ResultsItem> = emptyList(),
	@SerialName("total_results")
	val totalResults: Int? = null
)

@Serializable
data class ResultsItem(
	val overview: String? = null,
	@SerialName("original_language")
	val originalLanguage: String? = null,
	@SerialName("original_title")
	val originalTitle: String? = null,
	val video: Boolean? = null,
	val title: String? = null,
	@SerialName("genre_ids")
	val genreIds: List<Int>? = null,
	@SerialName("poster_path")
	val posterPath: String? = null,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	@SerialName("release_date")
	val releaseDate: String? = null,
	val popularity: Double? = null,
	@SerialName("vote_average")
	val voteAverage: Double? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	@SerialName("vote_count")
	val voteCount: Int? = null
)
