package com.example.aipoweredtravelplanner.domain.interactor

import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm
import com.example.aipoweredtravelplanner.domain.repository.TravelsRepository
import javax.inject.Inject

/**
 * Interactor class responsible for handling travels-related business logic.
 *
 * This class acts as a bridge between the presentation layer (e.g., ViewModel)
 * and the data layer (Repository). It encapsulates the logic for retrieving
 * travel itineraries based on a search query.
 *
 * @property repository The [TravelsRepository] instance used to fetch itineraries data.
 */
class TravelsInteractor @Inject constructor(
    private val repository: TravelsRepository
) {
    /**
     * Gets travel itineraries matching the given query.
     *
     * @param query The search term for the travel.
     * @return A list of matching [TravelItineraryDm]s, or an empty list if none are found.
     */
    suspend fun getTravelItineraries(query: String, timeQuery: String): List<TravelItineraryDm> {
        return repository.getTravelItineraries(query, timeQuery)
    }

    /**
     * Gets cached travel itineraries from the repository.
     *
     * @return A list of cached travel itineraries.
     */
    suspend fun getCachedTravelItineraries(): List<TravelItineraryDm> {
        return repository.getCachedTravelItineraries()
    }
}
