package com.katdmy.android.myfirstkotlinapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(
	@SerialName("original_language")
	val originalLanguage: String? = null,
	@SerialName("imdb_id")
	val imdbId: String? = null,
	val video: Boolean? = null,
	val title: String? = null,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	val revenue: Int? = null,
	val genres: List<GenresItem>? = null,
	val popularity: Double? = null,
	@SerialName("production_countries")
	val productionCountries: List<ProductionCountriesItem>? = null,
	val id: Int? = null,
	@SerialName("vote_count")
	val voteCount: Int? = null,
	val budget: Int? = null,
	val overview: String? = null,
	@SerialName("original_title")
	val originalTitle: String? = null,
	val runtime: Int? = null,
	@SerialName("poster_path")
	val posterPath: String? = null,
	@SerialName("spoken_languages")
	val spokenLanguages: List<SpokenLanguagesItem>? = null,
	@SerialName("production_companies")
	val productionCompanies: List<ProductionCompaniesItem>? = null,
	@SerialName("release_date")
	val releaseDate: String? = null,
	@SerialName("vote_average")
	val voteAverage: Double? = null,
	@SerialName("belongs_to_collection")
	val belongsToCollection: BelongsToCollection? = null,
	val tagline: String? = null,
	val adult: Boolean? = null,
	val homepage: String? = null,
	val status: String? = null
)

@Serializable
data class SpokenLanguagesItem(
	val name: String? = null,
	@SerialName("iso_639_1")
	val iso6391: String? = null,
	@SerialName("english_name")
	val englishName: String? = null
)

@Serializable
data class BelongsToCollection(
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	val name: String? = null,
	val id: Int? = null,
	@SerialName("poster_path")
	val posterPath: String? = null
)

@Serializable
data class ProductionCompaniesItem(
	@SerialName("logo_path")
	val logoPath: String? = null,
	val name: String? = null,
	val id: Int? = null,
	@SerialName("origin_country")
	val originCountry: String? = null
)

@Serializable
data class ProductionCountriesItem(
	@SerialName("iso_3166_1")
	val iso31661: String? = null,
	val name: String? = null
)
