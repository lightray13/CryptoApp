package com.testing.cryptoapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.cryptoapp.data.repository.main.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    init {
        loadCoinsFromApi(getTargetCurrency())
    }

    fun loadCoinsFromApi(targetCurrency: String = "usd") {
        viewModelScope.launch(repository.IODispatchersProvider()) {
            repository.coinsList(targetCurrency)
        }
    }

    fun getTargetCurrency(): String {
        return repository.targetCurrency()
    }

    fun putTargetCurrency(targetCurrency: String) {
        repository.putTargetCurrency(targetCurrency)
        loadCoinsFromApi(targetCurrency = targetCurrency)
    }

    fun setFilterBy(filterBy: Int) {
        repository.setFilterBy(filterBy)
    }
}