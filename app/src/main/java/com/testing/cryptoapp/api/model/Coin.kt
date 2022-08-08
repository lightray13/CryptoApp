package com.testing.cryptoapp.api.model

import com.google.gson.annotations.SerializedName

data class Coin(
    val symbol: String?,
    val id: String?,
    val name: String?,
    val image: String?,
    @SerializedName("current_price") val price: Double?
)