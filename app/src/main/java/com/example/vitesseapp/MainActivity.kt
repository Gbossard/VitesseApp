package com.example.vitesseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.core.analytics.AnalyticsTracker
import com.example.core.ui.analytics.LocalAnalyticsTracker
import com.example.core.ui.theme.VitesseAppTheme
import com.example.vitesseapp.ui.navigation.Home
import com.example.vitesseapp.ui.navigation.VitesseNavDisplay
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitesseAppTheme {
                CompositionLocalProvider(LocalAnalyticsTracker provides analyticsTracker) {
                    val backStack = rememberNavBackStack(Home)

                    val currentKey = backStack.lastOrNull()
                    LaunchedEffect(currentKey) {
                        currentKey?.let { key ->
                            val screenName = key::class.qualifiedName ?: key::class.simpleName.orEmpty()
                            analyticsTracker.trackScreen(screenName)
                        }
                    }

                    VitesseNavDisplay(
                        backStack = backStack
                    )
                }
            }
        }
    }
}
