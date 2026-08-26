package com.example.vitesseapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesseapp.data.local.CandidateEntity
import com.example.vitesseapp.data.repository.CandidateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val candidatesUiState: StateFlow<HomeUiState> = _searchQuery
        .toHomeUiState(scope = viewModelScope) { query ->
            candidateRepository.getAllCandidates(query = query)
        }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val favoritesUiState: StateFlow<HomeUiState> = _searchQuery
        .toHomeUiState(scope = viewModelScope) { query ->
            candidateRepository.getAllFavorites(query = query)
        }

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
private fun Flow<String>.toHomeUiState(
    scope: CoroutineScope,
    getCandidates: (String) -> Flow<List<CandidateEntity>>
): StateFlow<HomeUiState> =
    this
        .debounce(300.milliseconds)
        .flatMapLatest(getCandidates)
        .map { candidates ->
            if (candidates.isEmpty()) HomeUiState.Empty else HomeUiState.Success(candidates)
        }
        .onStart { emit(HomeUiState.Loading) }
        .catch { emit(HomeUiState.Error) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )