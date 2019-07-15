package com.guilhermelucas.moviedatabase.model

import com.guilhermelucas.domain.Genre
import com.guilhermelucas.domain.Movie
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import com.squareup.moshi.Json
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class MovieVO(
    val id: Int,
    val title: String,
    val overview: String?,
    val genres: List<Genre>?,
    val voteAverage: Double?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String?
)

