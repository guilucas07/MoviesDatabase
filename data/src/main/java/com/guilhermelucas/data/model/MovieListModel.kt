package com.guilhermelucas.data.model

import com.guilhermelucas.domain.Movie
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

fun Movie.toDataMovie(): MovieListModel {
    val genres = genres?.map { it.id }
    return MovieListModel(id, title, overview, voteAverage, genres, posterPath, backdropPath, releaseDate)
}

fun MovieListModel.toDomainMovie(): Movie {
    return Movie(id, title, overview, arrayListOf(), voteAverage, posterPath, backdropPath, releaseDate)
}