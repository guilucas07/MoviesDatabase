package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.api.MovieDataSource
import com.guilhermelucas.moviedatabase.data.Cache
import com.guilhermelucas.moviedatabase.model.Genre
import com.guilhermelucas.moviedatabase.model.Movie
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class HomeRepository(private val movieDataSource: MovieDataSource) {

    private var actualPage: Int = 0

    enum class RequestStrategy {
        FIRST_PAGE, NEXT_PAGE
    }

    fun getUpcomingMovies(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): Observable<List<Movie>> {

        val request = when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage + 1
            else -> 1
        }
        val genresObservable = movieDataSource.getGenres()
        val upcomingMoviesObservable = movieDataSource.getUpcomingMovies(request)

        return Observable.zip<List<Genre>, List<Movie>, List<Movie>>(
                genresObservable,
                upcomingMoviesObservable,
                BiFunction<List<Genre>, List<Movie>, List<Movie>> { genres, movies ->
                    actualPage++
                    val moviesWithGenres = movies.map { movie ->
                        movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    Cache.cacheMovies(moviesWithGenres)
                    moviesWithGenres
                })
    }

    fun getMovie(partialName: String): Observable<List<Movie>> {
        return movieDataSource.getMovie(partialName)
    }

    fun getDiscoveryMovies(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): Observable<List<Movie>> {

        val request = when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage + 1
            else -> 1
        }
        val genresObservable = movieDataSource.getGenres()
        val upcomingMoviesObservable = movieDataSource.getUpcomingMovies(request)

        return Observable.zip<List<Genre>, List<Movie>, List<Movie>>(
            genresObservable,
            upcomingMoviesObservable,
            BiFunction<List<Genre>, List<Movie>, List<Movie>> { genres, movies ->
                actualPage++
                val moviesWithGenres = movies.map { movie ->
                    movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
                }
                Cache.cacheMovies(moviesWithGenres)
                moviesWithGenres
            })
    }


}