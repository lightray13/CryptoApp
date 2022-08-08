package com.testing.cryptoapp.api

import com.testing.cryptoapp.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Result<out R> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val message: String): Result<Nothing>()

    object Loading: Result<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success [data = $data]"
            is Error -> "Error [message = $message]"
            is Loading -> "Loading"
        }
    }
}

val Result<*>.successed
    get() = this is Result.Success && data != null

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it.message ?: Constants.GENERIC_ERROR)) }}