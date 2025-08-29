package com.example.aipoweredtravelplanner.presentation

import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm

/**
 * Converts this [TravelItineraryDm] to a [TravelItineraryDetailsUiState].
 *
 * @param isFavorite Whether the itinerary is a favorite.
 * @return The [TravelItineraryDetailsUiState] representation.
 */
fun TravelItineraryDm.toTravelItineraryDetailsUiState(isFavorite: Boolean) = TravelItineraryDetailsUiState(
    id = id.orEmpty(),
    title = title,
    level = if(level.startsWith("#")) level else "#$level",
    program = program,
    isFavorite = isFavorite
)