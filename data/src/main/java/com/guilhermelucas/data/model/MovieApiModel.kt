package com.guilhermelucas.data.model

import com.guilhermelucas.domain.Movie
import com.squareup.moshi.Json
import java.util.*

data class MovieApiModel(
    val id: Int,
    val title: String,
    val overview: String?,
    @Json(name = "vote_average") val voteAverage: Double?,
    @Json(name = "genre_ids") val genreIds: List<Int>?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: Date?
)

fun Movie.toDataMovie(): MovieApiModel {
    val genres = genres?.map { it.id }
    return MovieApiModel(id, title, overview, voteAverage, genres, posterPath, backdropPath, releaseDate)
}

fun MovieApiModel.toDomainMovie(): Movie {
//    val genres = genres?.map { it.id }
    //TODO load genre list
    return Movie(id, title, overview, arrayListOf(), voteAverage, posterPath, backdropPath, releaseDate)
}