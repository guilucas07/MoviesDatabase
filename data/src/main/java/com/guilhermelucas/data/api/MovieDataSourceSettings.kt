package com.guilhermelucas.data.api

import com.guilhermelucas.data.firebase.RemoteConfig
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