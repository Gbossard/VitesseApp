package com.example.vitesseapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Home
@Serializable
data class EditCandidate(val candidateId: String? = null)