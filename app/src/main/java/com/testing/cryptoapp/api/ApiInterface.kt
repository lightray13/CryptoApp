package com.testing.cryptoapp.api

import com.testing.cryptoapp.api.model.Coin
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("coins/markets")
    suspend fun coinsList(@Query("vs_currency") targetCurrency: String): Response<List<Coin>>
}