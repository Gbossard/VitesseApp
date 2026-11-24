package com.example.feature.details.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailsCandidateRoute(
    candidateId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val viewModel = hiltViewModel<DetailsCandidateViewModel, DetailsCandidateViewModel.Factory>(
        creationCallback = { factory -> factory.create(candidateId) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailsCandidateScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onToggleFavorite = viewModel::toggleFavorite,
        onDeleteCandidate = viewModel::deleteCandidate
    )
}
