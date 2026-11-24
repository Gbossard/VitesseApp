package com.example.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    onFabClick: () -> Unit,
    onCandidateClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val candidatesUiState by viewModel.candidatesUiState.collectAsStateWithLifecycle()
    val favoritesUiState by viewModel.favoritesUiState.collectAsStateWithLifecycle()

    HomeScreen(
        searchQuery = searchQuery,
        candidatesUiState = candidatesUiState,
        favoritesUiState = favoritesUiState,
        onQueryChange = viewModel::onQueryChange,
        onFabClick = onFabClick,
        onCandidateClick = onCandidateClick
    )
}
