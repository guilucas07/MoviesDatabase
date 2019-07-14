package com.guilhermelucas.moviedatabase.model

import java.io.Serializable

data class PromotionAd(
    val title: String,
    val callToActionText: String,
    val searchKey: String
) : Serializable