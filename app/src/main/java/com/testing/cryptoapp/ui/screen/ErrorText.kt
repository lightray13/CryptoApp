package com.testing.cryptoapp.ui.coinsList

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ErrorText(
    title: String
) {
    val context = LocalContext.current
    Toast.makeText(
        context,
        title,
        Toast.LENGTH_SHORT
    ).show()
}