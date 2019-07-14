package com.guilhermelucas.moviedatabase.util

import com.guilhermelucas.data.firebase.RemoteConfig

class MovieImageUrlBuilder(private val remoteConfig: RemoteConfig) {

    fun buildPosterUrl(posterPath: String): String {
        return remoteConfig.getPosterUrl() + posterPath + "?api_key=" + remoteConfig.getApiKey()
    }

    fun buildBackdropUrl(backdropPath: String): String {
        return remoteConfig.getBackdropUrl() + backdropPath + "?api_key=" + remoteConfig.getApiKey()
    }
}
