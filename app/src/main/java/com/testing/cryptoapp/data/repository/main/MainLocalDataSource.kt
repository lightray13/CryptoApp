package com.testing.cryptoapp.data.repository.main

import com.testing.cryptoapp.data.local.database.CoinDatabase
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import javax.inject.Inject

class MainLocalDataSource @Inject constructor(private val database: CoinDatabase) {

    suspend fun insertCoinsIntoDatabase(coinsToInsert: List<CoinsListEntity>) {
        if (coinsToInsert.isNotEmpty()) {
            database.coinsListDao().insert(coinsToInsert)
        }
    }

    suspend fun favoriteSymbols(): List<String> = database.coinsListDao().favoriteSymbols()
}