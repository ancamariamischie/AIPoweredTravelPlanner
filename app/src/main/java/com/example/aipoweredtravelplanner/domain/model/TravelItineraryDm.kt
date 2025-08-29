package com.example.aipoweredtravelplanner.domain.model

/**
 * Represents a travel itinerary domain model.
 *
 * @property id The unique identifier for the travel itinerary. This can be null if the itinerary has not yet been persisted.
 * @property title The title or name of the travel itinerary.
 * @property level Describes the difficulty or experience level required for this itinerary.
 * @property program A list of strings, where each string represents a planned item or stage in the itinerary's program.
 *                 This could include destinations, activities, or general schedule points.
 */
data class TravelItineraryDm(
    val id: String?,
    val title: String,
    val level: String,
    val program: List<String>,
)