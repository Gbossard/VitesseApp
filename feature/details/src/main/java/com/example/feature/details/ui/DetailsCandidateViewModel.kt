package com.example.feature.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.local.CandidateEntity
import com.example.core.data.repository.CandidateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface DetailsCandidateUiState {
    data class Success(val candidate: CandidateEntity): DetailsCandidateUiState
    data object Error: DetailsCandidateUiState
    data object Loading: DetailsCandidateUiState
}

@HiltViewModel
class DetailsCandidateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    candidateRepository: CandidateRepository
): ViewModel() {
    private val candidateId: String = checkNotNull(savedStateHandle.get<String>("candidateId"))

    val uiState: StateFlow<DetailsCandidateUiState> = candidateRepository.getCandidateByIdFlow(candidateId)
        .map<CandidateEntity, DetailsCandidateUiState>  { candidate ->
            DetailsCandidateUiState.Success(candidate)
        }
        .onStart { emit(DetailsCandidateUiState.Loading) }
        .catch { emit(DetailsCandidateUiState.Error) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailsCandidateUiState.Loading
        )
}