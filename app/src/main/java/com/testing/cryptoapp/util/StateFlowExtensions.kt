package com.testing.cryptoapp.util

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.set(value: T, default: T) {
    this.value = value
    this.value = default
}