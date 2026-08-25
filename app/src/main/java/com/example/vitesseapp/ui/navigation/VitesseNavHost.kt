package com.example.vitesseapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitesseapp.ui.screens.add.AddCandidateScreen
import com.example.vitesseapp.ui.screens.home.HomeScreen

@RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 15)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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