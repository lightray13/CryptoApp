package com.testing.cryptoapp.data.repository.coinsList

import com.testing.cryptoapp.data.local.database.CoinDatabase
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinsListLocalDataSource @Inject constructor(private val database: CoinDatabase) {

    val allCoinsList: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsList()
    val coinsListByAscendingSymbol: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByAscendingSymbol(false)
    val coinsListByDescendingSymbol: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByDescendingSymbol(false)
    val coinsListByAscendingPrice: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByAscendingPrice(false)
    val coinsListByDescendingPrice: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByDescendingPrice(false)

    suspend fun updateFavoriteStatus(symbol: String): CoinsListEntity? {
        val coin = database.coinsListDao().coinFromSymbol(symbol)
        coin?.let {
            val coinsListEntity = CoinsListEntity(
                it.symbol,
                it.id,
                it.name,
                it.image,
                it.price,
                it.isFavorite.not()
            )

            if (database.coinsListDao().updateCoinsListEntity(coinsListEntity) > 0) {
                return coinsListEntity
            }
        }
        return null
    }
}