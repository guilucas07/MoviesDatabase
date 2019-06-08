package com.guilhermelucas.moviedatabase.detail.movie

import com.guilhermelucas.moviedatabase.api.MovieDataSource
import com.guilhermelucas.moviedatabase.data.Cache
import com.guilhermelucas.moviedatabase.model.Movie
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable

class DetailRepository(
    private val movieDataSource: MovieDataSource,
    private val imageUrlBuilder: MovieImageUrlBuilder
) {

    fun loadMovie(movieId: Long): Observable<MovieVO> {
        val cached = Cache.movies.findLast { it.id.toLong() == movieId }

        cached?.let { movie ->
            return Observable.fromArray(movie).map { createMovieVO(it) }
        }

        return movieDataSource.getMovie(movieId).map {
            createMovieVO(it)
        }
    }

    private fun createMovieVO(movie: Movie): MovieVO {
        var posterUrl: String? = null
        var backdropUrl: String? = null

        movie.posterPath?.let {
            posterUrl = imageUrlBuilder.buildPosterUrl(it)
        }

        movie.backdropPath?.let {
            backdropUrl = imageUrlBuilder.buildBackdropUrl(it)
        }

        val genres = if (!movie.genres.isNullOrEmpty()) {
            movie.genres
        } else {
            Cache.genres.filter { movie.genreIds?.contains(it.id) == true }
        }

        return MovieVO(
            movie.id,
            movie.title,
            movie.overview,
            genres,
            movie.voteAverage,
            posterUrl,
            backdropUrl,
            movie.releaseDate
        )
    }

}