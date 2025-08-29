package com.example.aipoweredtravelplanner.ui.details

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aipoweredtravelplanner.R
import com.example.aipoweredtravelplanner.presentation.TravelItineraryDetailsViewModel
import com.example.aipoweredtravelplanner.ui.theme.AppDimens
import com.example.aipoweredtravelplanner.presentation.TravelItineraryDetailsUiState

/**
 * Displays the details of a specific travel itinerary.
 *
 * Shows the itinerary's name, activities, destinations, and notes.
 * Allows marking the itinerary as a favorite and navigating back.
 *
 * @param viewModel Manages the screen's state.
 * @param itineraryId ID of the itinerary to display.
 * @param onBack Callback for when the back button is pressed.
 */
@Composable
fun TravelItineraryDetailsScreen(
    viewModel: TravelItineraryDetailsViewModel = hiltViewModel(),
    itineraryId: String,
    onBack: () -> Unit
) {
    val state: TravelItineraryDetailsUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onInit(itineraryId)
    }
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopBar(onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(AppDimens.spacingMedium)
            ) {
                TravelItineraryHeader(state) {
                    Toast.makeText(
                        context,
                        if (!viewModel.isFavorite()) "Itinerary added to favorites!"
                        else "Itinerary removed from favorites!",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.onFavoriteClick()
                }
                Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
                TravelItineraryActivities(activities = state.program)
            }
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back"
        )
    }
}

@Composable
private fun TravelItineraryHeader(
    state: TravelItineraryDetailsUiState,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = state.level,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
        IconButton(
            onClick = onFavoriteClick
        ) {
            if (state.isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.LightGray
                )
            }
        }
    }
}

@Composable
private fun TravelItineraryActivities(activities: List<String>) {
    Column {
        Text(
            text = stringResource(R.string.details_activities),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.size(AppDimens.spacingMedium))
        activities.forEach { activity ->
            Text(
                text = activity,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
        }
    }
}