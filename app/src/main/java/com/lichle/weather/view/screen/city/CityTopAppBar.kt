package com.lichle.weather.view.screen.city


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.lichle.weather.R
import com.lichle.weather.view.theme.appTopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTopAppBar(
    onBack: (() -> Unit)? = null,
    onSearchClick: (String) -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    fun handleSearchClicked() {
        onSearchClick(searchText)
        searchText = ""
    }

    TopAppBar(
        title = {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(text = stringResource(id = R.string.entrer_city_place_holder)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { handleSearchClicked() }
                )
            )
        },
        actions = {
            IconButton(onClick = { handleSearchClicked() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.menu_search)
                )
            }
        },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.menu_back)
                    )
                }
            }
        },
        colors = appTopAppBarColors()
    )
}