package com.guilhermelucas.data.model

import com.guilhermelucas.domain.Genre
import com.guilhermelucas.domain.Movie
import com.squareup.moshi.Json
import java.util.*

data class MovieListModel(
    val id: Int,
    val title: String,
    val overview: String?,
    @Json(name = "vote_average") val voteAverage: Double?,
    @Json(name = "genre_ids") val genreIds: List<Int>?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: Date?
)

data class MovieDetailModel(
    val id: Int,
    val title: String,
    val overview: String?,
    val genres : List<Genre>,
    @Json(name = "vote_average") val voteAverage: Double?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: Date?
)

fun Movie.toDataMovie(): MovieListModel {
    val genres = genres?.map { it.id }
    return MovieListModel(id, title, overview, voteAverage, genres, posterPath, backdropPath, releaseDate)
}

fun MovieDetailModel.toDomainMovie(): Movie {
    return Movie(id, title, overview, genres, voteAverage, posterPath, backdropPath, releaseDate)
}

fun MovieListModel.toDomainMovie(): Movie {
//    val genres = genres?.map { it.id }
    //TODO load genre list
    return Movie(id, title, overview, arrayListOf(), voteAverage, posterPath, backdropPath, releaseDate)
}