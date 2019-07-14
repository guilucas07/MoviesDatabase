package com.guilhermelucas.domain

import java.util.*

data class Movie(
    val id: Int,
    val title: String,
    val overview: String?,
    val genres: List<Genre>?,
    val voteAverage: Double?,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: Date?
)