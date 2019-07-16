package com.guilhermelucas.moviedatabase.detail

import com.guilhermelucas.data.api.MovieRemoteRepository
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.model.toMovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable

class DetailRepository(
    private val movieRepository: MovieRemoteRepository,
    private val imageUrlBuilder: MovieImageUrlBuilder
) {

    fun loadMovie(movieId: Long): Observable<MovieVO> {
//        val cached = Cache.movies.findLast { it.id.toLong() == movieId }
//
//        cached?.let { movie ->
//            return Observable.fromArray(movie).map { it.toMovieVO(imageUrlBuilder) }
//        }

        return movieRepository.getMovie(movieId).map {
            it.toMovieVO(imageUrlBuilder)
        }
    }

}