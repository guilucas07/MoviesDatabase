package com.guilhermelucas.moviedatabase.detail.movie

import com.guilhermelucas.data.api.MovieDataSource
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.model.toMovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable

class DetailRepository(
    private val movieDataSource: MovieDataSource,
    private val imageUrlBuilder: MovieImageUrlBuilder
) {

    fun loadMovie(movieId: Long): Observable<MovieVO> {
//        val cached = Cache.movies.findLast { it.id.toLong() == movieId }
//
//        cached?.let { movie ->
//            return Observable.fromArray(movie).map { it.toMovieVO(imageUrlBuilder) }
//        }

        return movieDataSource.getMovie(movieId).map {
            it.toMovieVO(imageUrlBuilder)
        }
    }

}