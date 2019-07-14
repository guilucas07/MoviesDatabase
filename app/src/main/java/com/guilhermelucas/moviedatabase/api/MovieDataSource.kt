package com.guilhermelucas.moviedatabase.api

import com.guilhermelucas.moviedatabase.domain.model.Genre
import com.guilhermelucas.moviedatabase.domain.model.Movie
import com.guilhermelucas.moviedatabase.domain.model.MoviesSearchResponse
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import io.reactivex.Observable
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
        tmdbApi.genres(dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage()).map { it.genres }


    fun getMovie(movieId: Long): Observable<Movie> {
        return tmdbApi.movie(movieId, dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage())
    }

    fun getMovie(fullOrPartialName: String): Observable<List<Movie>> {
        return tmdbApi
            .searchMovie(fullOrPartialName, dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage())
            .map { moviesSearchResponse: MoviesSearchResponse ->
                moviesSearchResponse.results
            }
    }

    fun getUpcomingMovies(page: Int = 1): Observable<List<Movie>> =
        tmdbApi.upcomingMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        ).map { it.results }

    fun getDiscoveryMovies(page: Int = 1): Observable<List<Movie>> =
        tmdbApi.discoveryMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        ).map { it.results }

    fun getTopRatedMovies(page: Int = 1): Observable<List<Movie>> =
        tmdbApi.topRatedMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        ).map { it.results }

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