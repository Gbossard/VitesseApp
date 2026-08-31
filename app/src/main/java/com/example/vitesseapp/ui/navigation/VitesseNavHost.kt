package com.example.vitesseapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.home.ui.HomeScreen
import com.example.feature.edit_page.ui.screens.AddCandidateScreen

@Composable
fun VitesseNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onFabClick = {
                    navController.navigate(AddCandidate)
                }
            )
        }
        composable<AddCandidate> {
            AddCandidateScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}