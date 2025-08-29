package com.example.aipoweredtravelplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * A composable function that displays a centered text message, typically used to indicate an empty state.
 *
 * @param modifier Optional [Modifier] for configuring the layout and appearance of the EmptyScreen.
 * @param text The text message to be displayed.
 */
@Composable
fun EmptyScreen(modifier: Modifier = Modifier, text: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}