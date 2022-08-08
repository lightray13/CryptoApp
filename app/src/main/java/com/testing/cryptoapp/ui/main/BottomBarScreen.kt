package com.testing.cryptoapp.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object CoinsList: BottomBarScreen(
        "coins_list",
        "Список валют",
        Icons.Default.List
    )

    object Favorites: BottomBarScreen(
        "favorites",
        "Избранное",
        Icons.Default.Favorite
    )
}