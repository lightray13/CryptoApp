package com.testing.cryptoapp.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.testing.cryptoapp.ui.coinsList.CoinsList
import com.testing.cryptoapp.ui.favorites.Favorites

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.CoinsList.route)  {
        composable(BottomBarScreen.CoinsList.route) {
            CoinsList()
        }
        composable(BottomBarScreen.Favorites.route) {
            Favorites()
        }
    }
}