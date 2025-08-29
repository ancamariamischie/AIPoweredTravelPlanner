package com.example.aipoweredtravelplanner.domain.repository

import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm

/**
 * Interface defining the contract for accessing and managing travels data.
 */
interface TravelsRepository {
    /**
     * Gets the list of travel itineraries currently stored in the cache.
     *
     * @return A list of cached [TravelItineraryDm] objects, or an empty list if none are cached.
     */
    suspend fun getCachedTravelItineraries(): List<TravelItineraryDm>

    /**
     * Fetches travel itineraries from the Gemini API based on the provided query.
     *
     * @param query A string containing a travel destination.
     * @return A list of [TravelItineraryDm] objects, or an empty list if fetching fails.
     */
    suspend fun getTravelItineraries(query: String, timeQuery: String): List<TravelItineraryDm>
}