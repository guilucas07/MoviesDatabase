package com.guilhermelucas.data.model

import com.guilhermelucas.domain.Movie

fun MovieDetailModel.toDomainMovie(): Movie {
    return Movie(id, title, overview, genres, voteAverage, posterPath, backdropPath, releaseDate)
}

fun Movie.toDataMovie(): MovieListModel {
    val genres = genres?.map { it.id }
    return MovieListModel(id, title, overview, voteAverage, genres, posterPath, backdropPath, releaseDate)
}

fun MovieListModel.toDomainMovie(): Movie {
    return Movie(id, title, overview, arrayListOf(), voteAverage, posterPath, backdropPath, releaseDate)
}