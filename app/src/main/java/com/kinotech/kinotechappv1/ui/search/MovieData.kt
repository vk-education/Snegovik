package com.kinotech.kinotechappv1.ui.search

data class SearchResults(
    val films: List<SimpleResult>
)

data class SimpleResult(
    val nameRu: String = "",
    val genres: List<Genres> = arrayListOf(),
    val year: String = "",
    val posterUrlPreview: String = "",
    val filmId: Int = 0,
    val countries: List<Countries> = arrayListOf()
)

data class Genres(
    val genre: String = ""
)

data class Countries(
    val country: String = ""
)

data class DescriptionRatingResults(
    val data: Description,
    val rating: Rating
)

data class Description(
    val description: String
)

data class Rating(
    val rating: Double,
    val ratingImdb: Double
)

data class Staff(
    val nameRu: String,
    val professionKey: String
)
