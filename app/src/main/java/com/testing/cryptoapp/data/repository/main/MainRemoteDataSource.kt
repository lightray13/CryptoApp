package com.testing.cryptoapp.data.repository.main

import com.testing.cryptoapp.api.Result
import com.testing.cryptoapp.api.ApiInterface
import com.testing.cryptoapp.api.BaseRemoteDataSource
import com.testing.cryptoapp.api.model.Coin
import javax.inject.Inject

class MainRemoteDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {

    suspend fun coinsList(targetCurrency: String): Result<List<Coin>> =
        getResult {
            service.coinsList(targetCurrency)
        }
}