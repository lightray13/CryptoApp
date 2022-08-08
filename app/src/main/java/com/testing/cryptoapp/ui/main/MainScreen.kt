package com.testing.cryptoapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.testing.cryptoapp.ui.main.BottomBarScreen
import com.testing.cryptoapp.ui.main.BottomNavGraph
import com.testing.cryptoapp.ui.main.MainViewModel
import com.testing.cryptoapp.util.Constants

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) }
    ) {
        BottomNavGraph(navController)
    }
}

@Composable
fun TopBar() {
    TopAppBar() {
        Spacer(Modifier.weight(1f, true))
        Spinner()
        Spacer(Modifier.weight(1f, true))
        DropDownMenu()
    }
}

@Composable
fun Spinner(mainViewModel: MainViewModel = hiltViewModel()) {
    val list = listOf("USD", "EUR", "RUB", "IDR", "KRW", "CNY", "TWD", "JPY")
    val expanded = remember { mutableStateOf(false) }
    val currentValue = remember { mutableStateOf(mainViewModel.getTargetCurrency()) }

    Box() {

        Row(modifier = Modifier
            .clickable {
                expanded.value = !expanded.value
            }
            .align(Alignment.Center)) {
            Text(text = currentValue.value)
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "show list of currency")

            DropdownMenu(expanded = expanded.value, onDismissRequest = {
                expanded.value = false
            }) {

                list.forEach {

                    DropdownMenuItem(onClick = {
                        currentValue.value = it
                        expanded.value = false
                        mainViewModel.putTargetCurrency(it)
                    }) {
                        Text(text = it)
                    }
                }
            }
        }
    }
}

@Composable
fun DropDownMenu(mainViewModel: MainViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "show menu")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Text("По умолчанию", fontSize=18.sp, modifier = Modifier.padding(10.dp)
                .clickable(onClick = {
                    mainViewModel.setFilterBy(Constants.COINS_FILTER_BY_DEFAULT)
                    expanded = false
                }))
            Divider()
            Text("Сортировка от A до Z", fontSize=18.sp, modifier = Modifier
                .padding(10.dp)
                .clickable(onClick = {
                    mainViewModel.setFilterBy(Constants.COINS_FILTER_BY_ASCENDING_SYMBOL)
                    expanded = false
                }))
            Text("Сортировка от Z до A", fontSize=18.sp, modifier = Modifier
                .padding(10.dp)
                .clickable(onClick = {
                    mainViewModel.setFilterBy(Constants.COINS_FILTER_BY_DESCENDING_SYMBOL)
                    expanded = false
                }))
            Divider()
            Text("По возрастанию цены", fontSize=18.sp, modifier = Modifier
                .padding(10.dp)
                .clickable(onClick = {
                    mainViewModel.setFilterBy(Constants.COINS_FILTER_BY_ASCENDING_PRICE)
                    expanded = false
                }))
            Text("По убыванию цены", fontSize=18.sp, modifier = Modifier
                .padding(10.dp)
                .clickable(onClick = {
                    mainViewModel.setFilterBy(Constants.COINS_FILTER_BY_DESCENDING_PRICE)
                    expanded = false
                }))
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.CoinsList,
        BottomBarScreen.Favorites
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}