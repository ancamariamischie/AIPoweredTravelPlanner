package com.example.aipoweredtravelplanner.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aipoweredtravelplanner.presentation.HomeViewModel
import com.example.aipoweredtravelplanner.ui.components.SearchBar
import com.example.aipoweredtravelplanner.ui.theme.AppDimens
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.aipoweredtravelplanner.R
import com.example.aipoweredtravelplanner.presentation.HomeScreenUiState
import com.example.aipoweredtravelplanner.presentation.TravelItineraryDetailsUiState
import com.example.aipoweredtravelplanner.presentation.TravelsUiState
import com.example.aipoweredtravelplanner.ui.components.EmptyScreen
import com.example.aipoweredtravelplanner.ui.components.ErrorAlertDialog
import com.example.aipoweredtravelplanner.ui.components.LoadingContent
import com.example.aipoweredtravelplanner.ui.components.SubmitButton
import com.example.aipoweredtravelplanner.ui.theme.Typography

/**
 * The main application screen.
 *
 * Handles searching for travel itineraries and viewing saved favorites.
 *
 * @param viewModel Manages UI state and logic for home and favorites.
 * @param onNavigateDetails Navigates to the itinerary details screen.
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onNavigateDetails: (String) -> Unit) {
    val state: HomeScreenUiState by viewModel.uiState.collectAsState()
    var currentTab by rememberSaveable { mutableStateOf(Screen.Home) }
    LaunchedEffect(Unit) {
        viewModel.onInit()
    }
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            HomeScreenTopBar()
        },
        bottomBar = {
            HomeScreenBottomBar(
                currentTab,
                onHomeClick = {
                    currentTab = Screen.Home
                    viewModel.initializeSuggestions(travelsState = state.travelsState)
                },
                onFavoritesClick = {
                    currentTab = Screen.Favorites
                    viewModel.initializeFavorites()
                }
            )
        }
    ) { padding ->
        HomeScreenContent(currentTab, padding, state, viewModel, onNavigateDetails)
    }
}

@Composable
private fun HomeScreenContent(
    currentTab: Screen,
    padding: PaddingValues,
    state: HomeScreenUiState,
    viewModel: HomeViewModel,
    onNavigateDetails: (String) -> Unit
) {
    if (currentTab == Screen.Home) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = AppDimens.spacingMedium)
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
            SearchBar(
                query = state.destination.value,
                onQueryChange = { viewModel.onDestinationQueryChange(it) },
                icon = Icons.Default.Place,
                placeholderText = stringResource(R.string.search_bar_placeholder),
                isError = state.destination.isError
            )
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
            SearchBar(
                query = state.duration.value,
                onQueryChange = { viewModel.onDurationQueryChange(it) },
                icon = Icons.Default.DateRange,
                keyboardType = KeyboardType.Number,
                placeholderText = stringResource(R.string.search_bar_placeholder_time),
                isError = state.duration.isError
            )
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
            if (viewModel.isError()) {
                Text(
                    text = stringResource(R.string.empty_inputs_error),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
            SubmitButton(stringResource(R.string.submit), viewModel::onSearchTravelItineraries)
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
            SuggestionsListContent(state, viewModel, onNavigateDetails)
        }
    } else {
        FavoritesListContent(state, padding, onNavigateDetails, viewModel)
    }
}

@Composable
private fun HomeScreenTopBar() {
    Text(
        modifier = Modifier.padding(AppDimens.spacingMedium),
        text = stringResource(R.string.title),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun FavoritesListContent(
    state: HomeScreenUiState,
    padding: PaddingValues,
    onNavigateDetails: (String) -> Unit,
    viewModel: HomeViewModel
) {
    if (state.favorites.isEmpty()) {
        EmptyScreen(Modifier.fillMaxSize(), stringResource(R.string.home_empty))
    } else {
        FavoritesListContent(
            padding,
            state,
            onNavigateDetails = { id -> onNavigateDetails(id) },
            onFavoriteClick = { viewModel.onFavoriteClick(it) }
        )
    }
}

@Composable
private fun SuggestionsListContent(
    state: HomeScreenUiState,
    viewModel: HomeViewModel,
    onNavigateDetails: (String) -> Unit
) {
    when (val currentState = state.travelsState) {
        TravelsUiState.Error -> {
            ErrorAlertDialog(viewModel)
        }

        TravelsUiState.Loading -> {
            LoadingContent()
        }

        is TravelsUiState.Suggestions -> {
            if (currentState.itineraries.isEmpty()) {
                EmptyScreen(
                    Modifier.fillMaxSize(),
                    stringResource(R.string.home_empty_suggestions)
                )
            } else {
                Spacer(Modifier.size(AppDimens.spacingMedium))
                TravelItinerariesListContent(
                    currentState,
                    onNavigateDetails = { id -> onNavigateDetails(id) },
                    onFavoriteClick = { viewModel.onFavoriteClick(it) }
                )
            }
        }

        else -> {
            EmptyScreen(Modifier.fillMaxSize(), stringResource(R.string.home_empty))
        }
    }
}

@Composable
private fun HomeScreenBottomBar(
    currentTab: Screen,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    NavigationBar {
        AppBottomNavigationBar(
            currentScreen = currentTab,
            onHomeClick = onHomeClick,
            onFavoritesClick = onFavoritesClick
        )
    }
}

@Composable
private fun FavoritesListContent(
    padding: PaddingValues,
    state: HomeScreenUiState,
    onNavigateDetails: (String) -> Unit,
    onFavoriteClick: (TravelItineraryDetailsUiState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppDimens.spacingMedium)
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.favorites_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
        state.favorites.forEach { itinerary ->
            TravelItineraryCard(
                title = itinerary.title,
                description = itinerary.level,
                onClick = {
                    onNavigateDetails(itinerary.id)
                },
                isFavorite = itinerary.isFavorite,
                onFavoriteClick = {
                    onFavoriteClick(itinerary)
                }
            )
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TravelItinerariesListContent(
    state: TravelsUiState.Suggestions,
    onNavigateDetails: (String) -> Unit,
    onFavoriteClick: (TravelItineraryDetailsUiState) -> Unit
) {
    var shouldShowBottomSheet by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.suggestions_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
            IconButton(
                onClick = {
                    shouldShowBottomSheet = true
                }
            ) {
                Icon(Icons.Outlined.Info, contentDescription = "Info")
            }
        }

        Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
        state.itineraries.forEach { itinerary ->
            TravelItineraryCard(
                title = itinerary.title,
                description = itinerary.level,
                onClick = {
                    onNavigateDetails(itinerary.id)
                },
                onFavoriteClick = {
                    onFavoriteClick(itinerary)
                },
                isFavorite = itinerary.isFavorite
            )
            Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
        }
        Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
    }
    if (shouldShowBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(0.5f),
            onDismissRequest = { shouldShowBottomSheet = false }) {
            Text(
                "These are the suggested itineraries. To add one of them to favorites, click on the heart icon.",
                style = Typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(AppDimens.spacingMedium)
            )
        }
    }
}

@Composable
fun AppBottomNavigationBar(
    currentScreen: Screen,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            isSelected = currentScreen == Screen.Home,
            onClick = onHomeClick
        )
        BottomNavItem(
            label = "Favorites",
            icon = Icons.Filled.Favorite,
            isSelected = currentScreen == Screen.Favorites,
            onClick = onFavoritesClick
        )
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = if (isSelected) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) else ButtonDefaults.textButtonColors()
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(Modifier.width(4.dp))
        Text(text = label)
    }
}

enum class Screen {
    Home,
    Favorites
}