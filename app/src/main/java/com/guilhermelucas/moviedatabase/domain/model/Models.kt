package com.guilhermelucas.moviedatabase.domain.model

import com.squareup.moshi.Json
import java.io.Serializable
import java.util.*

data class GenreResponse(val genres: List<Genre>)

data class Genre(val id: Int, val name: String)

data class MoviesSearchResponse(
    val page: Int,
    val results: List<Movie>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
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

data class PromotionAd(
    val title: String,
    val callToActionText: String,
    val searchKey: String
) : Serializable