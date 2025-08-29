package com.example.aipoweredtravelplanner

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aipoweredtravelplanner.ui.details.TravelItineraryDetailsScreen
import com.example.aipoweredtravelplanner.ui.home.HomeScreen
import com.example.aipoweredtravelplanner.ui.theme.AIPoweredTravelPlannerTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AIPoweredTravelPlannerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onNavigateDetails = { itineraryId ->
                navController.navigate("details/$itineraryId")
            })
        }
        composable(
            route = "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val itineraryId = backStackEntry.arguments?.getString("id").orEmpty()
            TravelItineraryDetailsScreen(
                itineraryId = itineraryId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}