package com.guilhermelucas.moviedatabase.model

import com.guilhermelucas.domain.Movie
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder

fun Movie.toMovieVO(imageUrlBuilder: MovieImageUrlBuilder): MovieVO {

    var posterUrl: String? = null
    var backdropUrl: String? = null

    posterPath?.let {
        posterUrl = imageUrlBuilder.buildPosterUrl(it)
    }

    backdropPath?.let {
        backdropUrl = imageUrlBuilder.buildBackdropUrl(it)
    }
    
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