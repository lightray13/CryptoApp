package com.testing.cryptoapp.util

import java.text.DecimalFormat

fun String?.emptyIfNull(): String {
    return this ?: ""
}

fun Double?.priceString(): String {
    return this?.let {
        val numberFormat = DecimalFormat("#,##0.00")
        numberFormat.format(this)
    } ?: ""
}