package com.guilhermelucas.moviedatabase.api

import com.guilhermelucas.moviedatabase.data.Cache
import com.guilhermelucas.moviedatabase.model.Genre
import com.guilhermelucas.moviedatabase.model.Movie
import com.guilhermelucas.moviedatabase.model.MoviesSearchResponse
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    fun getGenres(): Observable<List<Genre>> = Observable.create<List<Genre>> { emitter ->
        if (Cache.genres.isEmpty())
            tmdbApi.genres(dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    emitter.onNext(it.genres)
                    emitter.onComplete()
                }
        else {
            emitter.onNext(Cache.genres)
            emitter.onComplete()
        }
    }

    fun getMovie(movieId: Long): Observable<Movie> {
        return tmdbApi.movie(movieId, dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage())
            .doOnNext { movie ->
                Cache.cacheMovies(movie)
            }
    }

    fun getMovie(fullOrPartialName: String): Observable<List<Movie>> {
        return tmdbApi
            .searchMovie(fullOrPartialName, dataSourceSettings.getApiKey(), dataSourceSettings.getLanguage())
            .map { moviesSearchResponse: MoviesSearchResponse ->
                moviesSearchResponse.results
            }
    }

    fun getUpcomingMovies(page: Int = 1): Observable<List<Movie>> = Observable.create { emitter ->
        tmdbApi.upcomingMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Cache.cacheMovies(it.results)
                emitter.onNext(it.results)
                emitter.onComplete()
            }
    }

    fun getDiscoveryMovies(page: Int = 1): Observable<List<Movie>> = Observable.create { emitter ->
        tmdbApi.discoveryMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Cache.cacheMovies(it.results)
                emitter.onNext(it.results)
                emitter.onComplete()
            }
    }


    fun getTopRatedMovies(page: Int = 1): Observable<List<Movie>> = Observable.create { emitter ->
        tmdbApi.topRatedMovies(
            dataSourceSettings.getApiKey(),
            dataSourceSettings.getLanguage(),
            page,
            dataSourceSettings.getRegion()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Cache.cacheMovies(it.results)
                emitter.onNext(it.results)
                emitter.onComplete()
            }
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