package com.testing.cryptoapp.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinsListDao {

    @Query("SELECT * FROM coins_list")
    fun coinsList(): Flow<List<CoinsListEntity>>

    @Query("SELECT * FROM coins_list WHERE isFavorite = :isFavorite ORDER BY symbol ASC")
    fun coinsListByAscendingSymbol(isFavorite: Boolean): Flow<List<CoinsListEntity>>

    @Query("SELECT * FROM coins_list WHERE isFavorite = :isFavorite ORDER BY symbol DESC")
    fun coinsListByDescendingSymbol(isFavorite: Boolean): Flow<List<CoinsListEntity>>

    @Query("SELECT * FROM coins_list WHERE isFavorite = :isFavorite ORDER BY price ASC")
    fun coinsListByAscendingPrice(isFavorite: Boolean): Flow<List<CoinsListEntity>>

    @Query("SELECT * FROM coins_list WHERE isFavorite = :isFavorite ORDER BY price DESC")
    fun coinsListByDescendingPrice(isFavorite: Boolean): Flow<List<CoinsListEntity>>

    @Query("SELECT * FROM coins_list WHERE symbol = :symbol")
    suspend fun coinFromSymbol(symbol: String): CoinsListEntity?

    @Query("SELECT * FROM coins_list WHERE symbol = :symbol")
    fun coinFlowFromSymbol(symbol: String): Flow<CoinsListEntity>

    @Query("SELECT * FROM coins_list WHERE isFavorite = 1")
    fun favoriteCoins(): Flow<List<CoinsListEntity>>

    // список сокращенных имен всех избранных криптовалют
    @Query("SELECT symbol FROM coins_list WHERE isFavorite = 1")
    suspend fun favoriteSymbols(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CoinsListEntity>)

    @Update
    suspend fun updateCoinsListEntity(data: CoinsListEntity): Int

    @Query("DELETE FROM coins_list")
    suspend fun deleteAll()

}