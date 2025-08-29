package com.example.aipoweredtravelplanner.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.aipoweredtravelplanner.ui.components.shimmerLoadingAnimation
import com.example.aipoweredtravelplanner.ui.theme.AppDimens

/**
 * A Composable function that displays a travel itinerary card with a title, description,
 * and a favorite button.
 *
 * @param title The title of the travel itinerary.
 * @param description A brief description of the travel itinerary.
 * @param isFavorite A boolean indicating whether the itinerary is marked as a favorite.
 * @param onClick A lambda function to be executed when the card is clicked.
 * @param onFavoriteClick A lambda function to be executed when the favorite button is clicked.
 * @param isLoading A boolean indicating whether the card is in a loading state.
 * When true, shimmer animations are displayed (handled by TravelItineraryCardContent). Defaults to false.
 */
@Composable
fun TravelItineraryCard(
    title: String,
    description: String,
    isFavorite: Boolean,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(AppDimens.cardCornerRadius),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(AppDimens.cardElevation),
        onClick = onClick
    ) {
        TravelItineraryCardContent(
            title = title,
            description = description,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick,
            isLoading = isLoading
        )
    }
}

@Composable
private fun TravelItineraryCardContent(
    title: String,
    description: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .padding(AppDimens.spacingSmall),
            verticalArrangement = Arrangement.spacedBy(AppDimens.spacingMedium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = if (isLoading) Modifier
                    .fillMaxWidth(0.5f)
                    .shimmerLoadingAnimation()
                else Modifier,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = if (isLoading) Modifier
                    .fillMaxWidth(0.5f)
                    .shimmerLoadingAnimation()
                else Modifier
            )
        }
        FavoriteButton(onFavoriteClick, isFavorite)
        Spacer(modifier = Modifier.width(AppDimens.spacingSmall))
    }
}

@Composable
private fun FavoriteButton(onFavoriteClick: () -> Unit, isFavorite: Boolean) {
    val context = LocalContext.current
    IconButton(onClick = {
        onFavoriteClick()
        Toast.makeText(
            context,
            if (!isFavorite) "Itinerary added to favorites!" else "Itinerary removed from favorites!",
            Toast.LENGTH_SHORT
        ).show()
    }) {
        if (isFavorite) {
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
