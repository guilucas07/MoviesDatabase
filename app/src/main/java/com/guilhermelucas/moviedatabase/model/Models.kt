package com.guilhermelucas.moviedatabase.model

import com.squareup.moshi.Json
import java.util.*

data class GenreResponse(val genres: List<Genre>)

data class Genre(val id: Int, val name: String)

data class MoviesSearchResponse(
        val page: Int,
        val results: List<Movie>,
        @Json(name = "total_pages") val totalPages: Int,
        @Json(name = "total_results") val totalResults: Int
)

data class Movie(
        val id: Int,
        val title: String,
        val overview: String?,
        val genres: List<Genre>?,
        @Json(name = "vote_average") val voteAverage : Double?,
        @Json(name = "genre_ids") val genreIds: List<Int>?,
        @Json(name = "poster_path") val posterPath: String?,
        @Json(name = "backdrop_path") val backdropPath: String?,
        @Json(name = "release_date") val releaseDate: Date?
)

data class MovieVO(
        val id: Int,
        val title: String,
        val overview: String?,
        val genres: List<Genre>?,
        val voteAverage: Double?,
        val posterUrl: String?,
        val backdropUrl: String?,
        val releaseDate: Date?
)