package com.guilhermelucas.data.api

import com.guilhermelucas.data.model.MovieListModel
import com.guilhermelucas.data.model.toDomainMovie
import com.guilhermelucas.domain.Genre
import com.guilhermelucas.domain.Movie
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class MovieRemoteRepository(
    private val dataSource: MovieDataSource
) {

    fun getGenres(): Observable<List<Genre>> =
        dataSource.tmdbApi.genres(
            dataSource.dataSourceSettings.getApiKey(),
            dataSource.dataSourceSettings.getLanguage()
        ).map { it.genres }.cacheWithInitialCapacity(50)

    fun getMovie(movieId: Long): Observable<Movie> {
        return dataSource.tmdbApi.movie(movieId, dataSource.dataSourceSettings.getApiKey(), dataSource.dataSourceSettings.getLanguage())
            .map { it.toDomainMovie() }
    }

    fun getMovie(fullOrPartialName: String, loadGenres: Boolean = false): Observable<List<Movie>> {
        val searchList = dataSource.tmdbApi
            .searchMovie(fullOrPartialName, dataSource.dataSourceSettings.getApiKey(), dataSource.dataSourceSettings.getLanguage())
            .map { it.results }

        return if (loadGenres)
            mergeWithGenres(searchList)
        else
            searchList.map { it.map { it.toDomainMovie() } }
    }

    fun getUpcomingMovies(page: Int = 1, loadGenres: Boolean = false): Observable<List<Movie>> {
        val upcomingList = dataSource.tmdbApi.upcomingMovies(
            dataSource.dataSourceSettings.getApiKey(),
            dataSource.dataSourceSettings.getLanguage(),
            page,
            dataSource.dataSourceSettings.getRegion()
        ).map { it.results }

        return if (loadGenres)
            mergeWithGenres(upcomingList)
        else
            upcomingList.map { it.map { it.toDomainMovie() } }
    }

    fun getDiscoveryMovies(page: Int = 1, loadGenres: Boolean = false): Observable<List<Movie>> {
        val discoveryList = dataSource.tmdbApi.discoveryMovies(
            dataSource.dataSourceSettings.getApiKey(),
            dataSource.dataSourceSettings.getLanguage(),
            page,
            dataSource.dataSourceSettings.getRegion()
        ).map { it.results }

        return if (loadGenres)
            mergeWithGenres(discoveryList)
        else
            discoveryList.map { it.map { it.toDomainMovie() } }
    }

    fun getTopRatedMovies(page: Int = 1, loadGenres: Boolean = false): Observable<List<Movie>> {
        val topRated =
            dataSource.tmdbApi.topRatedMovies(
                dataSource.dataSourceSettings.getApiKey(),
                dataSource.dataSourceSettings.getLanguage(),
                page,
                dataSource.dataSourceSettings.getRegion()
            ).map { it.results }

        return if (loadGenres)
            mergeWithGenres(topRated)
        else
            topRated.map { it.map { it.toDomainMovie() } }
    }

    private fun mergeWithGenres(moviesList: Observable<List<MovieListModel>>): Observable<List<Movie>> {
        return Observable.zip<List<Genre>, List<MovieListModel>, List<Movie>>(
            getGenres(),
            moviesList,
            BiFunction<List<Genre>, List<MovieListModel>, List<Movie>> { genres, movies ->
                movies.map { movie ->
                    movie.toDomainMovie().copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
                }

            })
    }

    private fun addGenres(movie: Observable<MovieListModel>): Observable<Movie> {
        return Observable.zip<List<Genre>, MovieListModel, Movie>(
            getGenres(),
            movie,
            BiFunction<List<Genre>, MovieListModel, Movie> { genres, movieApi ->
                movieApi.toDomainMovie().copy(genres = genres.filter { movieApi.genreIds?.contains(it.id) == true })
            })
    }
}