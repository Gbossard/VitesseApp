package com.example.vitesseapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.feature.details.ui.screens.DetailsCandidateScreen
import com.example.feature.edit_page.ui.screens.EditCandidateScreen
import com.example.feature.home.ui.HomeScreen

@Composable
fun VitesseNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onFabClick = {
                    navController.navigate(EditCandidate())
                },
                onCandidateClick = { candidateId ->
                    navController.navigate(DetailsCandidate(candidateId = candidateId))
                }
            )
        }
        composable<EditCandidate> {
            EditCandidateScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.navigate(Home) {
                        popUpTo<Home> {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable<DetailsCandidate> {
            DetailsCandidateScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { candidateId ->
                    navController.navigate(EditCandidate(candidateId))
                }
            )
        }
    }
}