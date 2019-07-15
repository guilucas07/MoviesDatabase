package com.guilhermelucas.data.model

import com.squareup.moshi.Json

data class MoviesSearchResponse(
    val page: Int,
    val results: List<MovieListModel>,
    @field:Json(name = "total_pages") val totalPages: Int,
    @field:Json(name = "total_results") val totalResults: Int
)