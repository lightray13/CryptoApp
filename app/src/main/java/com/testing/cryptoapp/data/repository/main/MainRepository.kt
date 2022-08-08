package com.testing.cryptoapp.data.repository.main

import com.testing.cryptoapp.api.Result
import com.testing.cryptoapp.api.CoroutineDispatcherProvider
import com.testing.cryptoapp.api.successed
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import com.testing.cryptoapp.data.local.preferences.PreferenceStorage
import com.testing.cryptoapp.util.Constants
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainRemoteDataSource: MainRemoteDataSource,
    private val mainLocalDataSource: MainLocalDataSource,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val preferenceStorage: PreferenceStorage
) {

    fun targetCurrency(): String {
        return preferenceStorage.currency
    }

    fun putTargetCurrency(targetCurrency: String) {
        preferenceStorage.currency = targetCurrency
    }

    fun setFilterBy(filterBy: Int) {
        preferenceStorage.coinsFilterBy = filterBy
    }

    suspend fun coinsList(targetCurrency: String) {
        when (val result = mainRemoteDataSource.coinsList(targetCurrency)) {
            is Result.Success -> {
                if (result.successed) {
                    val favoriteSymbols = mainLocalDataSource.favoriteSymbols()

                    val customCoinList = result.data.let {
                        it.filter { item -> item.symbol.isNullOrEmpty().not()  }
                            .map { coin ->
                                CoinsListEntity(
                                    coin.symbol ?: "",
                                    coin.id,
                                    coin.name,
                                    coin.image,
                                    coin.price,
                                    favoriteSymbols.contains(coin.symbol)
                                )
                            }
                    }

                    mainLocalDataSource.insertCoinsIntoDatabase(customCoinList)

                    Result.Success(true)
                } else {
                    Result.Error(Constants.GENERIC_ERROR)
                }
            }
            else -> result as Result.Error
        }
    }

    fun IODispatchersProvider(): CoroutineDispatcher {
        return dispatcherProvider.IO()
    }
}