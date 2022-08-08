package com.testing.cryptoapp.ui.coinsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.cryptoapp.api.Result
import com.testing.cryptoapp.api.asResult
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import com.testing.cryptoapp.data.repository.coinsList.CoinsListRepository
import com.testing.cryptoapp.ui.state.CoinsUiState
import com.testing.cryptoapp.ui.state.FavoritesUiState
import com.testing.cryptoapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinsListViewModel @Inject constructor(private val repository: CoinsListRepository): ViewModel() {

    private val _uiState = MutableSharedFlow<CoinsUiState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiState: SharedFlow<CoinsUiState> = _uiState.asSharedFlow()

    private val _uiStateFavorites = MutableSharedFlow<FavoritesUiState>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiStateFavorites: SharedFlow<FavoritesUiState> = _uiStateFavorites.asSharedFlow()

    private val allCoinsList = repository.allCoinsList.asResult()
    private val coinsListByAscendingSymbol = repository.coinsListByAscendingSymbol.asResult()
    private val coinsListByDescendingSymbol = repository.coinsListByDescendingSymbol.asResult()
    private val coinsListByAscendingPrice = repository.coinsListByAscendingPrice.asResult()
    private val coinsListByDescendingPrice = repository.coinsListByDescendingPrice.asResult()

    init {
        filterByFlow()
    }

    fun filterByFlow() {
        viewModelScope.launch(repository.IODispatchersProvider()) {
            repository.coinsFilterByFlow.map {
                coinsListData()
            }.shareIn(
                this,
                replay = 0,
                started = SharingStarted.WhileSubscribed()
            ).collect()
        }
    }

    fun coinsListData() {
        when(getFilterBy()) {
            Constants.COINS_FILTER_BY_DEFAULT -> coinsListResult(allCoinsList)
            Constants.COINS_FILTER_BY_ASCENDING_SYMBOL -> coinsListResult(coinsListByAscendingSymbol)
            Constants.COINS_FILTER_BY_DESCENDING_SYMBOL -> coinsListResult(coinsListByDescendingSymbol)
            Constants.COINS_FILTER_BY_ASCENDING_PRICE -> coinsListResult(coinsListByAscendingPrice)
            Constants.COINS_FILTER_BY_DESCENDING_PRICE -> coinsListResult(coinsListByDescendingPrice)
        }
    }

    fun coinsListResult(coinsList: Flow<Result<List<CoinsListEntity>>>) {
        viewModelScope.launch(repository.IODispatchersProvider()) {
            coinsList.collectLatest { result ->
                val coinsUiState = when (result) {
                    is Result.Success -> CoinsUiState.Success(result.data)
                    is Result.Loading -> CoinsUiState.Loading
                    is Result.Error -> CoinsUiState.Error
                }
                _uiState.emit(coinsUiState)
            }
        }
    }

    fun getFilterBy(): Int {
        return repository.coinsFilterBy()
    }

    fun updateFavoriteStatus(symbol: String) {
        viewModelScope.launch(repository.IODispatchersProvider()) {
            val result = repository.updateFavoriteStatus(symbol)
            val favoritesUiState = if (result is Result.Success) {
                FavoritesUiState.Success(result.data)
            } else {
                FavoritesUiState.Error
            }
            _uiStateFavorites.emit(favoritesUiState)
        }
    }
}