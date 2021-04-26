package com.kinotech.kinotechappv1.ui.search

data class SearchResults(
    val results: List<SimpleResult>
)

data class SimpleResult(
    val nameRu: String,
    val genres: List<String>,
    val year: String,
    val posterUrlPreview: String
)

data class FullResult(
    val filmId: Int,
    val posterUrlPreview: String,
    val genres: List<String>,
    val year: String,
    val nameRu: String,
    val vote_average: Double,
    val vote_count: Int
)
