package com.lichle.weather.view.screen.city

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lichle.weather.R

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lichle.weather.view.ui_common.EmptyContent
import com.lichle.weather.view.ui_common.asString

@Composable
fun CityListScreen(
    onItemClick: ((CityUiModel) -> Unit)? = null,
    onSearchClick: ((String) -> Unit)? = null,
    viewModel: CityListViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CityListUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }

                is CityListUiEvent.NavigateToWeather -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onSearchClick?.invoke(event.cityName)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FavoriteTopAppBar(
                onSearchClick = { cityName ->
                    viewModel.processIntent(CityListIntent.SearchCityWeather(cityName))
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        CityListContent(
            uiState = uiState,
            onItemClick = onItemClick,
            onDeleteCity = { city ->
                viewModel.processIntent(CityListIntent.DeleteCity(city.id))
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun CityListContent(
    uiState: CityListUiState,
    onItemClick: ((CityUiModel) -> Unit)?,
    onDeleteCity: (CityUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            uiState.isLoading -> LoadingContent()
            uiState.cities.isNotEmpty() -> CityList(
                cities = uiState.cities,
                onDelete = onDeleteCity,
                onItemClick = { city -> onItemClick?.invoke(city) }
            )

            else -> EmptyContent(stringResource(id = R.string.empty_favorite_list))
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun CityList(
    cities: List<CityUiModel>,
    onDelete: (CityUiModel) -> Unit,
    onItemClick: (CityUiModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("CityList"),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(cities) { city ->
            CityItem(
                city = city,
                onDelete = onDelete,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun CityItem(
    city: CityUiModel,
    onDelete: (CityUiModel) -> Unit,
    onItemClick: (CityUiModel) -> Unit
) {
    ElevatedCard(
        onClick = { onItemClick(city) },
        modifier = Modifier
            .padding(4.dp)
            .testTag("CityItem"),
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
            IconButton(modifier = Modifier.testTag("BtnDelete"), onClick = { onDelete(city) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_button),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}