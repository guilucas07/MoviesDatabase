package com.guilhermelucas.moviedatabase.domain.model

import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import com.squareup.moshi.Json
import java.util.*

data class Movie(
    val id: Int,
    val title: String,
    val overview: String?,
    val genres: List<Genre>?,
    @Json(name = "vote_average") val voteAverage: Double?,
    @Json(name = "genre_ids") val genreIds: List<Int>?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: Date?
)

fun Movie.toMovieVO(imageUrlBuilder: MovieImageUrlBuilder): MovieVO {

    var posterUrl: String? = null
    var backdropUrl: String? = null

    posterPath?.let {
        posterUrl = imageUrlBuilder.buildPosterUrl(it)
    }

    backdropPath?.let {
        backdropUrl = imageUrlBuilder.buildBackdropUrl(it)
    }

//    val genres = if (!genres.isNullOrEmpty()) {
//        genres
//    } else {
//        Cache.genres.filter { genreIds?.contains(it.id) == true }
//    }

    return MovieVO(
        id,
        title,
        overview,
        genres,
        voteAverage,
        posterUrl,
        backdropUrl,
        releaseDate
    )
}