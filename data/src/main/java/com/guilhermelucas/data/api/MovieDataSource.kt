package com.guilhermelucas.data.api

import com.guilhermelucas.data.model.MovieListModel
import com.guilhermelucas.data.model.toDomainMovie
import com.guilhermelucas.domain.Genre
import com.guilhermelucas.domain.Movie
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieDataSource private constructor() {

    private val moshi = Moshi.Builder().add(CustomDateAdapter())
    private var dataSourceSettings = MovieDataSourceSettings()

    private val tmdbApi: TmdbApi = Retrofit.Builder()
        .baseUrl(dataSourceSettings.getServerUrl())
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TmdbApi::class.java)

    companion object {
        val instance = MovieDataSource()
    }

    fun getGenres(): Observable<List<Genre>> =
        tmdbApi.genres(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage()
        ).map { it.genres }.cacheWithInitialCapacity(50)

    fun getMovie(movieId: Long): Observable<Movie> {
        return tmdbApi.movie(movieId, dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage()).map { it.toDomainMovie() }
    }

    fun getMovie(fullOrPartialName: String, loadGenres: Boolean = false): Observable<List<Movie>> {
        val searchList = tmdbApi
            .searchMovie(fullOrPartialName, dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage())
            .cacheWithInitialCapacity(50)
            .map { it.results }

        return if (loadGenres)
            mergeWithGenres(searchList)
        else
            searchList.map { it.map { it.toDomainMovie() } }
    }

    fun getUpcomingMovies(page: Int = 1, loadGenres: Boolean = false): Observable<List<Movie>> {
        val upcomingList = tmdbApi.upcomingMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        ).map { it.results }

        return if (loadGenres)
            mergeWithGenres(upcomingList)
        else
            upcomingList.map { it.map { it.toDomainMovie() } }
    }

    fun getDiscoveryMovies(page: Int = 1, loadGenres: Boolean = false): Observable<List<Movie>> {
        val discoveryList = tmdbApi.discoveryMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        ).map { it.results }

        return if (loadGenres)
            mergeWithGenres(discoveryList)
        else
            discoveryList.map { it.map { it.toDomainMovie() } }
    }

    fun getTopRatedMovies(page: Int = 1, loadGenres: Boolean = false): Observable<List<Movie>> {
        val topRated =
            tmdbApi.topRatedMovies(
                dataSourceSettings.getApiKey(),
                dataSourceSettings.getLanguage(),
                page,
                dataSourceSettings.getRegion()
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

    private class CustomDateAdapter {
        internal val dateFormat: DateFormat

        init {
            dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        }

        @ToJson
        @Synchronized
        internal fun dateToJson(d: Date): String {
            return dateFormat.format(d)
        }

        @FromJson
        @Synchronized
        internal fun dateToJson(s: String): Date {
            if (s.isNotEmpty())
                return dateFormat.parse(s)
            return Date()
        }
    }


}