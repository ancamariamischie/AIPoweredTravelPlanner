package com.example.aipoweredtravelplanner.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Stores and manages favorite travels using DataStore.
 *
 * @property context The application context.
 */
class FavoritesDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "user_preferences")
        private val FAVORITES_KEY = stringSetPreferencesKey("favorites")
    }

    private val gson = Gson()

    private fun serialize(travelItinerary: TravelItineraryDm): String = gson.toJson(travelItinerary)

    private fun deserialize(json: String): TravelItineraryDm = gson.fromJson(json, TravelItineraryDm::class.java)

    val favoritesFlow: Flow<Set<TravelItineraryDm>> = context.dataStore.data
        .map { preferences ->
            val jsonSet = preferences[FAVORITES_KEY] ?: emptySet()
            jsonSet.map { deserialize(it) }.toSet()
        }

    /**
     * Adds a travel itinerary to the favorites set.
     * */
    suspend fun likeTravelItinerary(travelItinerary: TravelItineraryDm) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[FAVORITES_KEY] ?: emptySet()
            val newSet = currentSet + serialize(travelItinerary)
            preferences[FAVORITES_KEY] = newSet
        }
    }

    /**
     * Removes an itinerary by identifier.
     */
    suspend fun unlikeTravelItinerary(itineraryId: String) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[FAVORITES_KEY] ?: emptySet()
            val filteredSet = currentSet
                .map { deserialize(it) }
                .filter { it.id != itineraryId }
                .map { serialize(it) }
                .toSet()
            preferences[FAVORITES_KEY] = filteredSet
        }
    }
}
