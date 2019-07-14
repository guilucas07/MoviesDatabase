package com.guilhermelucas.data.api

import com.guilhermelucas.data.model.GenreResponse
import com.guilhermelucas.data.model.MovieDetailModel
import com.guilhermelucas.data.model.MovieListModel
import com.guilhermelucas.data.model.MoviesSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("genre/movie/list")
    fun genres(
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Observable<GenreResponse>

    @GET("movie/upcoming")
    fun upcomingMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Int,
            @Query("region") region: String
    ): Observable<MoviesSearchResponse>

    @GET("discover/movie")
    fun discoveryMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Observable<MoviesSearchResponse>

    @GET("movie/{id}")
    fun movie(
            @Path("id") id: Long,
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Observable<MovieDetailModel>

    @GET("search/movie")
    fun searchMovie(
            @Query("query") movieName: String,
            @Query("api_key") apiKey: String,
            @Query("language") language: String
    ): Observable<MoviesSearchResponse>

    @GET("movie/popular")
    fun topRatedMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Int,
            @Query("region") region: String
    ): Observable<MoviesSearchResponse>


}
