package com.testing.cryptoapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoinsListEntity::class], version = 1, exportSchema = false)
abstract class CoinDatabase: RoomDatabase() {
    abstract fun coinsListDao(): CoinsListDao

    companion object{
        fun buildDatabase(context: Context): CoinDatabase {
            return Room.databaseBuilder(context, CoinDatabase::class.java, "Coins").build()
        }
    }
}