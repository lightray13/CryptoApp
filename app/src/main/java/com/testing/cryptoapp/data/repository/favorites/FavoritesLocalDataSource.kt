package com.testing.cryptoapp.data.repository.favorites

import com.testing.cryptoapp.data.local.database.CoinDatabase
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesLocalDataSource @Inject constructor(private val database: CoinDatabase) {
    val favoriteCoins: Flow<List<CoinsListEntity>> = database.coinsListDao().favoriteCoins()
    val coinsListByAscendingSymbol: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByAscendingSymbol(true)
    val coinsListByDescendingSymbol: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByDescendingSymbol(true)
    val coinsListByAscendingPrice: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByAscendingPrice(true)
    val coinsListByDescendingPrice: Flow<List<CoinsListEntity>> = database.coinsListDao().coinsListByDescendingPrice(true)

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