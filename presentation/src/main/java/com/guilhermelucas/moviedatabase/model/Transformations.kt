package com.guilhermelucas.moviedatabase.model

import com.guilhermelucas.domain.Movie
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import java.text.SimpleDateFormat
import java.util.*


fun Movie.toMovieVO(imageUrlBuilder: MovieImageUrlBuilder): MovieVO {

    var posterUrl: String? = null
    var backdropUrl: String? = null

    posterPath?.let {
        posterUrl = imageUrlBuilder.buildPosterUrl(it)
    }

    backdropPath?.let {
        backdropUrl = imageUrlBuilder.buildBackdropUrl(it)
    }

    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(releaseDate)

    return MovieVO(
        id,
        title,
        overview,
        genres,
        voteAverage,
        posterUrl,
        backdropUrl,
        formattedDate
    )
}