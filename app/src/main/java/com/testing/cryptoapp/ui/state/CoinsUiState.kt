package com.testing.cryptoapp.ui.state

import com.testing.cryptoapp.data.local.database.CoinsListEntity

sealed class CoinsUiState {
    data class Success(val coinsList: List<CoinsListEntity>): CoinsUiState()
    object Error: CoinsUiState()
    object Loading: CoinsUiState()
    object Empty: CoinsUiState()
}