package com.testing.cryptoapp.data.repository.coinsList

import com.testing.cryptoapp.api.CoroutineDispatcherProvider
import com.testing.cryptoapp.api.Result
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import com.testing.cryptoapp.data.local.preferences.PreferenceStorage
import com.testing.cryptoapp.util.Constants
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CoinsListRepository @Inject constructor(
    private val coinsListLocalDataSource: CoinsListLocalDataSource,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val preferenceStorage: PreferenceStorage
) {

    val allCoinsList = coinsListLocalDataSource.allCoinsList
    val coinsListByAscendingSymbol = coinsListLocalDataSource.coinsListByAscendingSymbol
    val coinsListByDescendingSymbol = coinsListLocalDataSource.coinsListByDescendingSymbol
    val coinsListByAscendingPrice = coinsListLocalDataSource.coinsListByAscendingPrice
    val coinsListByDescendingPrice = coinsListLocalDataSource.coinsListByDescendingPrice

    val coinsFilterByFlow = preferenceStorage.coinsFilterByFlow

    suspend fun updateFavoriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = coinsListLocalDataSource.updateFavoriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }

    fun coinsFilterBy(): Int {
        return preferenceStorage.coinsFilterBy
    }

    fun IODispatchersProvider(): CoroutineDispatcher {
        return dispatcherProvider.IO()
    }
}