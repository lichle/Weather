package com.lichle.weather.view.ui_common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveSnackBar(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String = "",
    duration: SnackbarDuration = SnackbarDuration.Short,
    onDismiss: (() -> Unit)? = null
) {
    // Launch a coroutine to show the snackbar
    LaunchedEffect(snackbarHostState, message) {
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        )

        // Handle dismissal or action clicks
        if (result == SnackbarResult.ActionPerformed && onDismiss != null) {
            onDismiss()
        }
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        strokeWidth = 4.dp
    )
}