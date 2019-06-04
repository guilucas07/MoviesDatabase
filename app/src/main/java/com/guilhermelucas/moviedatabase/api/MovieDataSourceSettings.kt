package com.guilhermelucas.moviedatabase.api

import com.guilhermelucas.moviedatabase.firebase.RemoteConfig
import java.util.*

class MovieDataSourceSettings {

    private val ignoreUserLocalization = RemoteConfig.instance.ignoreUserLocalization()

    fun getLanguage(): String {
        return if (ignoreUserLocalization)
            RemoteConfig.instance.getDefaultLanguage()
        else
            Locale.getDefault().toString()
    }

    fun getRegion(): String {
        return if (ignoreUserLocalization)
            RemoteConfig.instance.getDefaultRegion()
        else
            Locale.getDefault().country
    }

    fun getServerUrl(): String {
        return RemoteConfig.instance.getServerUrl()
    }

    fun getApiKey(): String {
        return RemoteConfig.instance.getApiKey()
    }
}