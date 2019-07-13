package com.guilhermelucas.moviedatabase.data

import com.guilhermelucas.moviedatabase.domain.model.Genre
import com.guilhermelucas.moviedatabase.domain.model.Movie

object Cache {

    private const val moviesCacheLimit = 150
    var genres = listOf<Genre>()
        private set
    var movies = listOf<Movie>()
        private set

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }

    fun cacheMovies(movies: List<Movie>) {
        val mutableList = Cache.movies.toMutableList()
        val initial = if (mutableList.size - moviesCacheLimit > 0) mutableList.size - moviesCacheLimit else 0
        this.movies = mutableList.subList(initial, mutableList.size).apply {
            addAll(movies)
        }
    }

    fun cacheMovies(movie: Movie) {
        this.movies = movies.toMutableList().apply {
            add(movie)
        }
    }
}
