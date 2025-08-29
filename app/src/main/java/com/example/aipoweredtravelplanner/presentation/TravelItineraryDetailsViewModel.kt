package com.example.aipoweredtravelplanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aipoweredtravelplanner.data.datastore.FavoritesDataStore
import com.example.aipoweredtravelplanner.domain.interactor.TravelsInteractor
import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Manages UI state for the Travel Itinerary Details screen.
 *
 * Fetches itineraries data, handles favouring, and updates the UI.
 *
 * @property interactor Fetches travel itineraries data.
 * @property dataStore Manages favorite travel itineraries.
 */
@HiltViewModel
class TravelItineraryDetailsViewModel @Inject constructor(
    private val interactor: TravelsInteractor,
    private val dataStore: FavoritesDataStore
) : ViewModel() {
    private val _uiState: MutableStateFlow<TravelItineraryDetailsUiState> =
        MutableStateFlow(TravelItineraryDetailsUiState())
    private val _favorites = MutableStateFlow<Set<TravelItineraryDm>>(emptySet())
    val uiState = _uiState.asStateFlow()
    private var travelItineraryDm: TravelItineraryDm? = null

    init {
        viewModelScope.launch {
            dataStore.favoritesFlow.collect { newFavorites ->
                _favorites.value = newFavorites
            }
        }
    }

    /**
     * Loads itinerary details for the given [travelId] when the ViewModel is initialized.
     *
     * It first checks cached travel itineraries, then favorites. If found, it updates the UI state.
     *
     * @param travelId The identifier of the itinerary to load.
     */
    fun onInit(travelId: String) {
        viewModelScope.launch {
            val itineraryFromFavorites = _favorites.value.find { it.id == travelId }
            val itinerary = interactor.getCachedTravelItineraries().find { it.id == travelId }
                ?: itineraryFromFavorites

            itinerary?.let { retrievedTravelItinerary ->
                _uiState.update {
                    retrievedTravelItinerary.toTravelItineraryDetailsUiState(
                        isFavorite = itineraryFromFavorites != null
                    )
                }
                travelItineraryDm = retrievedTravelItinerary
            }
        }
    }

    /**
     * Toggles the favorite status of the current itinerary.
     *
     * If the itinerary is already a favorite, it's removed.
     * Otherwise, it's added to favorites.
     */
    fun onFavoriteClick() {
        viewModelScope.launch {
            if (isFavorite()) {
                unlikeTravelItinerary()
            } else {
                likeItinerary()
            }
        }
    }

    /**
     * Checks if the current item is in the list of favorites.
     *
     * @return True if the item is a favorite, false otherwise.
     */
    fun isFavorite(): Boolean {
        return _favorites.value.find { it.id == _uiState.value.id } != null
    }

    private suspend fun likeItinerary() {
        travelItineraryDm?.let { itinerary ->
            dataStore.likeTravelItinerary(itinerary)
            _uiState.update {
                it.copy(
                    isFavorite = true
                )
            }
        }
    }

    private suspend fun unlikeTravelItinerary() {
        dataStore.unlikeTravelItinerary(_uiState.value.id)
        _uiState.update {
            it.copy(
                isFavorite = false
            )
        }
    }
}

