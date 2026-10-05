package com.example.feature.details.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.local.CandidateEntity
import com.example.core.data.repository.CandidateRepository
import com.example.core.data.storage.PhotoStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface SalaryError {
    data object NetworkError : SalaryError
    data object RateNotFound : SalaryError
    data object Unknown : SalaryError
}

sealed interface DetailsCandidateUiState {
    data class Success(
        val candidate: CandidateEntity,
        val salaryInGbp: Double? = null,
        val salaryError: SalaryError? = null
    ): DetailsCandidateUiState
    data object Error: DetailsCandidateUiState
    data object Loading: DetailsCandidateUiState
}

@HiltViewModel
class DetailsCandidateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val candidateRepository: CandidateRepository,
    private val photoStorage: PhotoStorage
): ViewModel() {
    private val candidateId: String = checkNotNull(savedStateHandle.get<String>("candidateId"))

    val uiState: StateFlow<DetailsCandidateUiState> = candidateRepository.getCandidateByIdFlow(candidateId)
        .map<CandidateEntity, DetailsCandidateUiState>  { candidate ->
            val result = candidateRepository.convertSalaryToGbp(candidate.salary)
            result.fold(
                onSuccess = { salaryInGbp ->
                    DetailsCandidateUiState.Success(
                        candidate = candidate,
                        salaryInGbp = salaryInGbp,
                        salaryError = null
                    )
                },
                onFailure = { exception ->
                    val errorType = when(exception) {
                        is IOException -> SalaryError.NetworkError
                        is NoSuchElementException -> SalaryError.RateNotFound
                        else -> SalaryError.Unknown
                    }
                    DetailsCandidateUiState.Success(
                        candidate = candidate,
                        salaryInGbp = null,
                        salaryError = errorType
                    )
                }
            )
        }
        .onStart { emit(DetailsCandidateUiState.Loading) }
        .catch { emit(DetailsCandidateUiState.Error) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailsCandidateUiState.Loading
        )

    fun toggleFavorite() {
        viewModelScope.launch {
            candidateRepository.toggleFavorite(candidateId)
        }
    }

    fun deleteCandidate(candidate: CandidateEntity) {
        viewModelScope.launch {
            candidateRepository.deleteCandidate(candidate)
            candidate.photo.let { photoStorage.deletePhoto(it) }
        }
    }
}