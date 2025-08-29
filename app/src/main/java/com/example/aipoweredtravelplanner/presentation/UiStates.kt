package com.example.aipoweredtravelplanner.presentation

import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm

/**
 * UI state for the Home screen.
 *
 * @param destination Current destination search input and validation state.
 * @param duration Current duration input and validation state for the travel itinerary.
 * @param travelsState State of the list of travel itineraries resulting from the search.
 *                     (e.g., loading, success with data, error, or empty).
 * @param favorites List of travel itineraries marked as favorites.
 */
data class HomeScreenUiState(
    val destination: HomeScreenInputUiState = HomeScreenInputUiState(),
    val duration: HomeScreenInputUiState = HomeScreenInputUiState(),
    val travelsState: TravelsUiState = TravelsUiState.Empty,
    val favorites:  List<TravelItineraryDetailsUiState> = emptyList()
)

/**
 * Represents the different states of the Travels UI.
 */
sealed class TravelsUiState {
    data object Empty: TravelsUiState()
    data object Loading : TravelsUiState()
    data object Error : TravelsUiState()
    data class Suggestions(val itineraries: List<TravelItineraryDetailsUiState>) : TravelsUiState()
}

/**
 * UI state for an input field on the HomeScreen.
 *
 * @property value The current text in the input field.
 * @property isError True if the input has an error, false otherwise.
 */
data class HomeScreenInputUiState(
    val value: String = "",
    val isError: Boolean = false
)

/**
 * Holds the details for displaying a travel itinerary.
 *
 * @property id Unique ID of the itinerary.
 * @property title Name of the itinerary.
 * @property level Itinerary level.
 * @property program List of planned activities.
 * @property isFavorite True if the user marked it as a favorite.
 */
data class TravelItineraryDetailsUiState(
    val id: String = "",
    val title: String = "",
    val level: String = "",
    val program: List<String> = emptyList(),
    val isFavorite: Boolean = false
) {
    fun toTravelItineraryDm(): TravelItineraryDm {
        return TravelItineraryDm(
            id = id,
            title = title,
            level = level,
            program = program
        )
    }
}