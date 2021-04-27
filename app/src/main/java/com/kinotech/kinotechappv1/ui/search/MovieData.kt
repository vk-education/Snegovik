package com.kinotech.kinotechappv1.ui.search

data class SearchResults(
    val films: List<SimpleResult>
)

data class SimpleResult(
    val nameRu: String,
    val genres: List<Genres>,
    val year: String,
    val posterUrlPreview: String
)

data class Genres(
    val genre : String
)

data class FullResult(
    val filmId: Int,
    val posterUrlPreview: String,
    val genres: List<String>,
    val year: String,
    val nameRu: String,
    val voteAverage: Double,
    val voteCount: Int
)
