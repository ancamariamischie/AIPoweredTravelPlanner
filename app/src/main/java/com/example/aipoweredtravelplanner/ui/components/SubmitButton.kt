package com.example.aipoweredtravelplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.aipoweredtravelplanner.ui.theme.AppDimens
import com.example.aipoweredtravelplanner.ui.theme.Typography

@Composable
fun SubmitButton(text: String, onSubmit: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onSubmit,
            shape = RoundedCornerShape(AppDimens.buttonCornerRadius)
        ) {
            Text(
                modifier = Modifier.padding(AppDimens.spacingSmall),
                text = text,
                style = Typography.bodyMedium,
            )
        }
    }
}