package com.example.vitesseapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.details.ui.screens.DetailsCandidateScreen
import com.example.feature.home.ui.HomeScreen
import com.example.feature.edit_page.ui.screens.EditCandidateScreen

@Composable
fun VitesseNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onFabClick = {
                    navController.navigate(EditCandidate())
                },
                onCandidateClick = {
                    navController.navigate(DetailsCandidate)
                }
            )
        }
        composable<EditCandidate> {
            EditCandidateScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.navigate(Home)
                }
            )
        }
        composable<DetailsCandidate> {
            DetailsCandidateScreen()
        }
    }
}