package com.lichle.weather.view.screen.city

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.lichle.weather.R

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.lichle.weather.view.ui_common.EmptyContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    navController: NavHostController,
    viewModel: CityListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(CityListIntent.LoadFavorites)
    }

    Scaffold(
        topBar = {
            FavoriteTopAppBar(
                onSearchClick = { cityName ->
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.processIntent(CityListIntent.SearchCity(cityName))
                    navController.navigate("weatherByName/$cityName") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                when (state) {
                    is CityListState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    is CityListState.Success -> {
                        val cities = (state as CityListState.Success).cities
                        if (cities.isNotEmpty()) {
                            FavoriteList(
                                cities = cities,
                                onDelete = { city ->
                                    viewModel.processIntent(CityListIntent.DeleteCity(city.id))
                                },
                                onItemClick = { city ->
                                    viewModel.processIntent(
                                        CityListIntent.NavigateToWeatherDetail(city.id)
                                    )
                                    navController.navigate("weatherById/${city.id}")
                                }
                            )
                        } else {
                            EmptyContent(stringResource(id = R.string.empty_favorite_list))
                        }
                    }
                    is CityListState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (state as CityListState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FavoriteList(
    cities: List<CityUiModel>,
    onDelete: (CityUiModel) -> Unit,
    onItemClick: (CityUiModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("FavoriteList"),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(cities) { city ->
            FavoriteItem(
                city = city,
                onDelete = onDelete,
                onItemClick = onItemClick
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoriteItem(
    city: CityUiModel,
    onDelete: (CityUiModel) -> Unit,
    onItemClick: (CityUiModel) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                onDelete(city)
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier.testTag("BtnDelete"),
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_button),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        },
        dismissContent = {
            ElevatedCard(
                onClick = { onItemClick(city) },
                modifier = Modifier.padding(4.dp).testTag("FavoriteItem"),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = city.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = city.country,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { onDelete(city) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete_button),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    )
}