package com.guilhermelucas.data.model

import com.squareup.moshi.Json
import java.util.*

data class MovieListModel(
    val id: Int,
    val title: String,
    val overview: String?,
    @field:Json(name = "vote_average") val voteAverage: Double?,
    @field:Json(name = "genre_ids") val genreIds: List<Int>?,
    @field:Json(name = "poster_path") val posterPath: String?,
    @field:Json(name = "backdrop_path") val backdropPath: String?,
    @field:Json(name = "release_date") val releaseDate: Date?
)
