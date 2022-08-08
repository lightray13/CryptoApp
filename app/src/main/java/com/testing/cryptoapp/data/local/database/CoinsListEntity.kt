package com.testing.cryptoapp.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins_list")
data class CoinsListEntity (
    @PrimaryKey val symbol: String,
    val id: String?,
    val name: String?,
    val image: String?,
    val price: Double?,
    val isFavorite: Boolean
)