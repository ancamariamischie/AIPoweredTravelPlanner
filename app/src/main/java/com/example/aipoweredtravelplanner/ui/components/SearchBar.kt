package com.example.aipoweredtravelplanner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.aipoweredtravelplanner.ui.theme.AppDimens

private val TextFieldHeight = 56.dp

/**
 * A reusable Composable component that displays a search bar containing a text field and a search button.
 *
 * @param query The current search query string.
 * @param onQueryChange A callback function that is invoked when the search query changes.
 * @param icon The icon to be displayed at the start of the text field.
 * @param placeholderText The text to be displayed as a placeholder when the search field is empty.
 * @param keyboardType The type of keyboard to be shown when the text field is focused. Defaults to
 * [KeyboardType.Text].
 * @param isError Indicates whether the input is currently in an error state.
 * If true, the text field will be visually highlighted to indicate an error. Defaults to false.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    icon: ImageVector,
    placeholderText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(text = placeholderText) },
            singleLine = true,
            shape = RoundedCornerShape(AppDimens.cardCornerRadius),
            modifier = Modifier
                .fillMaxWidth()
                .height(TextFieldHeight),
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "Place"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Next,
                keyboardType = keyboardType
            ),
            isError = isError
        )
    }
}
