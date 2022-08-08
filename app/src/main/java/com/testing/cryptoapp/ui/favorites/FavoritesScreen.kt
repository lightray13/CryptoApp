package com.testing.cryptoapp.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.testing.cryptoapp.data.local.database.CoinsListEntity
import com.testing.cryptoapp.ui.coinsList.ErrorText
import com.testing.cryptoapp.ui.screen.LoadingIndicator
import com.testing.cryptoapp.ui.screen.MessageText
import com.testing.cryptoapp.ui.state.CoinsUiState
import com.testing.cryptoapp.ui.state.FavoritesUiState
import com.testing.cryptoapp.ui.theme.Shapes
import com.testing.cryptoapp.util.Constants
import com.testing.cryptoapp.util.emptyIfNull
import com.testing.cryptoapp.util.priceString

@Composable
fun Favorites(favoritesViewModel: FavoritesViewModel = hiltViewModel()) {
    Scaffold {
        val uiState: CoinsUiState by favoritesViewModel.uiState.collectAsState(CoinsUiState.Loading)
        val uiStateFavorites: FavoritesUiState by favoritesViewModel.uiStateFavorites.collectAsState(FavoritesUiState.Empty)

        if (uiState is CoinsUiState.Success) FavoritesCoinList((uiState as CoinsUiState.Success).coinsList)
        else if (uiState is CoinsUiState.Error) ErrorText(title = Constants.GENERIC_ERROR)
        else if (uiState is CoinsUiState.Loading) LoadingIndicator()

        if (uiStateFavorites is FavoritesUiState.Success) MessageText(title =
            "${((uiStateFavorites as FavoritesUiState.Success).coinsListEntity.symbol)} удален из Избранного")
        else if (uiStateFavorites is FavoritesUiState.Error) ErrorText(title = Constants.GENERIC_ERROR)
    }
}

    @Composable
    fun FavoritesCoinList(coinsList: List<CoinsListEntity>) {
        if(coinsList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Вы еще не добавили\nкриптовалюты в Избранное",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }
        LazyColumn{
            itemsIndexed(items = coinsList) {_, item ->
                FavoriteCoinsListItem(coinsListEntity = item)
            }
        }
    }

    @Composable
    fun FavoriteCoinsListItem(coinsListEntity: CoinsListEntity, favoritesViewModel: FavoritesViewModel = hiltViewModel()) {
        Card(modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min), shape = Shapes.medium, elevation = 4.dp) {
            Surface() {
                Row(
                    Modifier
                        .padding(4.dp)
                        .fillMaxSize()) {
                    Image(
                        painter = rememberImagePainter(data = coinsListEntity.image,
                            builder = {
                                scale(Scale.FILL)
                                placeholder(coil.compose.base.R.drawable.notification_action_background)
                                transformations(CircleCropTransformation())
                            }),
                        contentDescription = "coins list entity image",
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.2f)
                            .padding(16.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight()
                            .weight(0.8f)
                    ) {
                        Text(text = coinsListEntity.symbol,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold)
                        Text(text = coinsListEntity.name.emptyIfNull(),
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Thin,
                            modifier = Modifier
                                .padding(4.dp))
                        Text(text = coinsListEntity.price.priceString(),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.body1 )
                    }
                    IconToggleButton(checked = true,
                        onCheckedChange = {
                            favoritesViewModel.updateFavoriteStatus(coinsListEntity.symbol)
                        }
                    ) {
                        Icon(tint = Color.Red,
                            modifier = Modifier.graphicsLayer {
                                scaleX = 1.3f
                                scaleY = 1.3f
                            },
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "add coin to favorites")
                    }
                }
            }
        }
    }