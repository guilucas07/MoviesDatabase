package com.guilhermelucas.data.model

import com.guilhermelucas.domain.Genre
import com.squareup.moshi.Json

data class GenreResponse(val genres: List<Genre>)

data class MoviesSearchResponse(
    val page: Int,
    val results: List<MovieApiModel>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)