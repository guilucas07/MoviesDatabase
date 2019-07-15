package com.guilhermelucas.data.model

import com.guilhermelucas.domain.Genre
import com.guilhermelucas.domain.Movie
import com.squareup.moshi.Json
import java.util.*

data class MovieDetailModel(
    val id: Int,
    val title: String,
    val overview: String?,
    val genres : List<Genre>,
    @field:Json(name = "vote_average") val voteAverage: Double?,
    @field:Json(name = "poster_path") val posterPath: String?,
    @field:Json(name = "backdrop_path") val backdropPath: String?,
    @field:Json(name = "release_date") val releaseDate: Date?
)

fun MovieDetailModel.toDomainMovie(): Movie {
    return Movie(id, title, overview, genres, voteAverage, posterPath, backdropPath, releaseDate)
}
