package com.guilhermelucas.data.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.guilhermelucas.data.BuildConfig
import com.guilhermelucas.data.R

class RemoteConfig private constructor() {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    companion object {
        val instance = RemoteConfig()
    }

    private object FirebaseKeys {
        const val SERVER_URL = "server_url"
        const val API_KEY = "api_key"
        const val DEFAULT_REGION = "request_default_region"
        const val DEFAULT_LANGUAGE = "request_default_language"
        const val IGNORE_LOCALIZATION = "request_ignore_user_localization"
        const val POSTER_URL = "poster_url"
        const val BACKDROP_URL = "backdrop_url"
        const val PROMOTION_ITEM_INTERVAL = "promotion_item_interval"
    }

    init {
        remoteConfig.setDefaults(R.xml.firebase_config_defaults)
        if (BuildConfig.DEBUG) {
            val settings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .setFetchTimeoutInSeconds(60)
                .build()
            remoteConfig.setConfigSettings(settings)
        }

        remoteConfig.fetchAndActivate()
    }

    fun getServerUrl(): String {
        return remoteConfig.getString(FirebaseKeys.SERVER_URL)
    }

    fun getApiKey(): String {
        return remoteConfig.getString(FirebaseKeys.API_KEY)
    }

    fun getDefaultLanguage(): String {
        return remoteConfig.getString(FirebaseKeys.DEFAULT_LANGUAGE)
    }

    fun getDefaultRegion(): String {
        return remoteConfig.getString(FirebaseKeys.DEFAULT_REGION)
    }

    fun ignoreUserLocalization(): Boolean {
        return remoteConfig.getBoolean(FirebaseKeys.IGNORE_LOCALIZATION)
    }

    fun getPosterUrl(): String {
        return remoteConfig.getString(FirebaseKeys.POSTER_URL)
    }

    fun getBackdropUrl(): String {
        return remoteConfig.getString(FirebaseKeys.BACKDROP_URL)
    }

    fun getPromotionItemInterval(): Long {
        return remoteConfig.getLong(FirebaseKeys.PROMOTION_ITEM_INTERVAL)
    }
}