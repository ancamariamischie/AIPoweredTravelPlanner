package com.example.aipoweredtravelplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.aipoweredtravelplanner.R
import com.example.aipoweredtravelplanner.presentation.HomeViewModel
import com.example.aipoweredtravelplanner.ui.theme.AppDimens

/**
 * Displays an error alert dialog.
 *
 * This dialog is shown when an error occurs and provides an "OK" button to dismiss it.
 *
 * @param viewModel The [HomeViewModel] instance to interact with, specifically for dismissing the error.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ErrorAlertDialog(viewModel: HomeViewModel) {
    BasicAlertDialog(
        modifier = Modifier.wrapContentSize(),
        onDismissRequest = {
            viewModel.onErrorDismissed()
        },
        content = {
            Surface(shape = RoundedCornerShape(20.dp)) {
                Spacer(Modifier.size(AppDimens.spacingMedium))
                Column(
                    modifier = Modifier.padding(AppDimens.spacingMedium),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyScreen(
                        text = stringResource(R.string.home_error)
                    )
                    SubmitButton(
                        stringResource(R.string.ok),
                        viewModel::onErrorDismissed
                    )
                }
                Spacer(Modifier.size(AppDimens.spacingMedium))
            }
        }
    )
}