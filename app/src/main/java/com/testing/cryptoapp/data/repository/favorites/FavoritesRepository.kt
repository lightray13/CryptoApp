package com.testing.cryptoapp.data.repository.favorites

import com.testing.cryptoapp.api.CoroutineDispatcherProvider
import com.testing.cryptoapp.api.Result
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import com.testing.cryptoapp.data.local.preferences.PreferenceStorage
import com.testing.cryptoapp.util.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val preferenceStorage: PreferenceStorage
) {

    val favoriteCoins: Flow<List<CoinsListEntity>> = favoritesLocalDataSource.favoriteCoins
    val coinsListByAscendingSymbol = favoritesLocalDataSource.coinsListByAscendingSymbol
    val coinsListByDescendingSymbol = favoritesLocalDataSource.coinsListByDescendingSymbol
    val coinsListByAscendingPrice = favoritesLocalDataSource.coinsListByAscendingPrice
    val coinsListByDescendingPrice = favoritesLocalDataSource.coinsListByDescendingPrice

    val coinsFilterByFlow = preferenceStorage.coinsFilterByFlow

    suspend fun updateFavoriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = favoritesLocalDataSource.updateFavoriteStatus(symbol)
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