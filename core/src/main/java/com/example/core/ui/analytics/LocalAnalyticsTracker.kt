package com.example.core.ui.analytics

import androidx.compose.runtime.compositionLocalOf
import com.example.core.analytics.AnalyticsTracker

val LocalAnalyticsTracker = compositionLocalOf<AnalyticsTracker> {
    error("No AnalyticsTracker found. Make sure to provide it using CompositionLocalProvider.")
}

object AnalyticsScreens {
    const val HOME = "HomeScreen"
    const val DETAILS_CANDIDATE = "DetailsCandidateScreen"
    const val EDIT_CANDIDATE = "EditCandidateScreen"
}