package com.example.vitesseapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.core.analytics.AnalyticsTracker
import com.example.core.ui.analytics.LocalAnalyticsTracker
import com.example.core.ui.theme.VitesseAppTheme
import com.example.vitesseapp.ui.navigation.VitesseNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitesseAppTheme {
                CompositionLocalProvider(LocalAnalyticsTracker provides analyticsTracker) {
                    val navController = rememberNavController().apply {
                        DisposableEffect(this) {
                            val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                                destination.route?.let { route ->
                                    analyticsTracker.trackScreen(route)
                                }
                            }
                            addOnDestinationChangedListener(listener)

                            onDispose {
                                removeOnDestinationChangedListener(listener)
                            }
                        }
                    }

                    VitesseNavHost(
                        navController = navController
                    )
                }
            }
        }
    }
}