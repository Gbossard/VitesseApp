package com.example.vitesseapp.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class EditCandidate(val candidateId: String? = null) : NavKey

@Serializable
data class DetailsCandidate(val candidateId: String) : NavKey
