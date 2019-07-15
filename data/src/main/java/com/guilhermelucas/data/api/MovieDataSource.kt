package com.guilhermelucas.data.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieDataSource private constructor(val dataSourceSettings: MovieDataSourceSettings) {

    private val moshi = Moshi.Builder().add(CustomDateAdapter())

    val tmdbApi: TmdbApi = Retrofit.Builder()
        .baseUrl(dataSourceSettings.getServerUrl())
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TmdbApi::class.java)

    companion object {
        @Volatile
        private var INSTANCE: MovieDataSource? = null

        fun getInstance(remoteConfig: MovieDataSourceSettings): MovieDataSource =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: MovieDataSource(remoteConfig).also { INSTANCE = it }
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