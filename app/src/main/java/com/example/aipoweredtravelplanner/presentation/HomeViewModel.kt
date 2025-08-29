package com.example.aipoweredtravelplanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aipoweredtravelplanner.data.datastore.FavoritesDataStore
import com.example.aipoweredtravelplanner.domain.model.TravelItineraryDm
import com.example.aipoweredtravelplanner.domain.interactor.TravelsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen.
 *
 * This ViewModel is responsible for managing the state of the Home screen and handling user interactions.
 * It uses a [TravelsInteractor] to fetch itineraries data.
 *
 * @property interactor The [TravelsInteractor] used to fetch travels data.
 * @property dataStore The [FavoritesDataStore] used to manage favorite itineraries.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: TravelsInteractor,
    private val dataStore: FavoritesDataStore
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState())
    private val _favorites = MutableStateFlow<Set<TravelItineraryDm>>(emptySet())
    private val decimalRegex = Regex("^\\d+\$")
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.favoritesFlow.collect { newFavorites ->
                _favorites.value = newFavorites
            }
        }
    }

    /**
     * Sets up the UI when the screen loads.
     *
     * It always prepares favorite itineraries.
     * If the current state is to show suggestions, it also prepares and displays those.
     */
    fun onInit() {
        initializeFavorites()
        when (val travelsState = _uiState.value.travelsState) {
            is TravelsUiState.Suggestions -> {
                initializeSuggestions(travelsState)
            }

            else -> {}
        }
    }

    /**
     * Clears the error and sets the travels state to empty.
     */
    fun onErrorDismissed() {
        _uiState.update {
            it.copy(
                travelsState = TravelsUiState.Empty
            )
        }
    }


    /**
     * Updates the search query and reloads favorites if the query is empty.
     *
     * @param query The new search string.
     */
    fun onDestinationQueryChange(query: String) {
        _uiState.update {
            it.copy(
                destination = _uiState.value.destination.copy(value = query)
            )
        }
    }

    /**
     * Updates the search query and reloads favorites if the query is empty.
     *
     * @param query The new search string.
     */
    fun onDurationQueryChange(query: String) {
        if (!query.matches(decimalRegex) && query.isNotEmpty()) return
        _uiState.update {
            it.copy(
                duration = _uiState.value.duration.copy(value = query)
            )
        }
    }

    /**
     * Starts searching for travel itineraries.
     *
     * It shows a loading indicator while fetching and displaying itineraries results.
     */
    fun onSearchTravelItineraries() {
        validate()
        if (isError()) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    travelsState = TravelsUiState.Loading
                )
            }
            searchTravelItinerariesAndUpdateUi()
        }
    }

    /**
     * Toggles the favorite status of an itinerary and updates the UI.
     *
     * @param travelItinerary The travel itinerary to toggle the favorite status for.
     */
    fun onFavoriteClick(travelItinerary: TravelItineraryDetailsUiState) {
        viewModelScope.launch {
            val isCurrentlyLiked = _favorites.value.any { it.id == travelItinerary.id }
            if (isCurrentlyLiked) {
                dataStore.unlikeTravelItinerary(travelItinerary.id)
            } else {
                dataStore.likeTravelItinerary(travelItinerary.toTravelItineraryDm())
            }
            initializeFavorites()
            when (val currentState = _uiState.value.travelsState) {
                is TravelsUiState.Suggestions -> {
                    onChangeFavoriteStateSuggestion(currentState, travelItinerary, isCurrentlyLiked)
                }

                else -> {}
            }
        }
    }

    /**
     * Loads itinerary suggestions and marks any favorites.
     *
     * If new suggestions are provided, this updates the UI.
     * It also checks each suggestion against the user's favorites
     * and flags them accordingly.
     *
     * @param travelsState The suggestions to display.
     */
    fun initializeSuggestions(travelsState: TravelsUiState) {
        if (travelsState !is TravelsUiState.Suggestions) return
        _uiState.update {
            it.copy(
                travelsState = TravelsUiState.Suggestions(
                    travelsState.itineraries.map { itinerary ->
                        itinerary.copy(
                            isFavorite = _favorites.value.any { it.id == itinerary.id }
                        )
                    }
                )
            )
        }
    }

    /**
     * Loads favorite itineraries into the UI.
     *
     * It takes the current list of favorites, marks them as favorites,
     * and updates the UI state to display them.
     */
    fun initializeFavorites() {
        _uiState.update {
            it.copy(
                favorites = _favorites.value.toList().map { itinerary ->
                    itinerary.toTravelItineraryDetailsUiState(isFavorite = true)
                }
            )
        }
    }

    /**
     * Checks if the current UI state has an error in destination or duration.
     *
     * @return True if there's an error, false otherwise.
     */
    fun isError(): Boolean {
        return _uiState.value.destination.isError || _uiState.value.duration.isError
    }

    private fun onChangeFavoriteStateSuggestion(
        currentState: TravelsUiState.Suggestions,
        travelItinerary: TravelItineraryDetailsUiState,
        isCurrentlyLiked: Boolean
    ) {
        _uiState.update {
            it.copy(
                travelsState = TravelsUiState.Suggestions(
                    itineraries = currentState.itineraries.updateFavoriteStatus(
                        travelId = travelItinerary.id,
                        isFavorite = !isCurrentlyLiked
                    )
                )
            )
        }
    }

    private suspend fun searchTravelItinerariesAndUpdateUi() {
        try {
            val result = interactor.getTravelItineraries(
                _uiState.value.destination.value,
                _uiState.value.duration.value
            )
            _uiState.update {
                it.copy(
                    travelsState = TravelsUiState.Suggestions(
                        itineraries = result.map { itinerary ->
                            itinerary.toTravelItineraryDetailsUiState(false)
                        }
                    )
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    travelsState = TravelsUiState.Error
                )
            }
        }
    }

    private fun validate() {
        val destinationState = _uiState.value.destination
        val durationState = _uiState.value.duration
        _uiState.update {
            it.copy(
                destination = destinationState.copy(isError = destinationState.value.isBlank()),
                duration = durationState.copy(isError = durationState.value.isBlank())
            )
        }
    }

    private fun List<TravelItineraryDetailsUiState>.updateFavoriteStatus(
        travelId: String,
        isFavorite: Boolean
    ): List<TravelItineraryDetailsUiState> {
        return this.map {
            if (it.id == travelId) {
                it.copy(isFavorite = isFavorite)
            } else {
                it
            }
        }
    }
}
