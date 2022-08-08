package com.testing.cryptoapp.ui.state

import com.testing.cryptoapp.data.local.database.CoinsListEntity

sealed class FavoritesUiState {
    data class Success(val coinsListEntity: CoinsListEntity): FavoritesUiState()
    object Error: FavoritesUiState()
    object Empty: FavoritesUiState()
}