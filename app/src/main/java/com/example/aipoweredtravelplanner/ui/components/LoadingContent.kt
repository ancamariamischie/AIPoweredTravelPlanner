package com.example.aipoweredtravelplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.aipoweredtravelplanner.ui.home.TravelItineraryCard
import com.example.aipoweredtravelplanner.ui.theme.AppDimens

/**
 * Shows a loading screen with three placeholder travel itinerary cards.
 *
 * Use this when content is loading to indicate activity to the user.
 * Displays three `TravelItineraryCard` instances in their loading state,
 * arranged vertically with medium spacing.
 */
@Composable
fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.spacingMedium)
    ) {
        repeat(3) {
            TravelItineraryCard(title = "", description = "", isLoading = true, isFavorite = false)
        }
    }
}