package com.example.vitesseapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesseapp.data.local.CandidateEntity
import com.example.vitesseapp.data.repository.CandidateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val candidates: List<CandidateEntity>): HomeUiState
    data object Error: HomeUiState
    data object Empty: HomeUiState
    data object Loading: HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    candidateRepository: CandidateRepository
): ViewModel() {
    val uiState: StateFlow<HomeUiState> = candidateRepository.getAllCandidates()
        .map { candidates ->
            if (candidates.isEmpty()) {
                HomeUiState.Empty
            } else {
                HomeUiState.Success(candidates)
            }
        }
        .onStart { emit(HomeUiState.Loading) }
        .catch {
            emit(HomeUiState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )
}