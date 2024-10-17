package com.lichle.weather.view.ui_common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun EmptyContent(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Info, // Default icon, can be changed
    buttonText: String? = null, // Optional action button text
    onButtonClick: (() -> Unit)? = null // Optional button click action
) {
    Column(
        modifier = modifier
            .testTag("EmptyContent")
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Decorative, no need for description
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary // Use Material3 color scheme
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge, // Use bodyLarge for the message
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Less focused color
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Optional button if action is provided
        buttonText?.let {
            Button(
                onClick = { onButtonClick?.invoke() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = buttonText)
            }
        }
    }
}