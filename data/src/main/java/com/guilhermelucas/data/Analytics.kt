package com.guilhermelucas.data

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

abstract class Analytics(context: Context) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logEvent(event: String, params: Bundle = Bundle()) {
        firebaseAnalytics.logEvent(event, params)
    }
}