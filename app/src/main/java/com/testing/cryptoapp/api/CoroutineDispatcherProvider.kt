package com.testing.cryptoapp.api

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineDispatcherProvider @Inject constructor() {

    fun IO() = Dispatchers.IO
    fun Default() = Dispatchers.Default
    fun Main() = Dispatchers.Main

}