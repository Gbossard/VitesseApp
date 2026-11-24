package com.example.feature.edit_page.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.edit_page.R

@Composable
fun EditCandidateRoute(
    candidateId: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val viewModel =
        hiltViewModel<EditCandidateViewModel, EditCandidateViewModel.Factory>(
            creationCallback = { factory -> factory.create(candidateId) }
        )
    val editUiState by viewModel.editUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is EditUiEvent.SaveSuccess -> {
                    onSaveClick()
                }
                is EditUiEvent.SaveErrorEvent -> {
                    val resourcesId = when (event.error) {
                        is SaveError.DatabaseError -> R.string.form_error_save_database
                        is SaveError.FileNotFound -> R.string.form_error_save_file_not_found
                        is SaveError.StorageFull -> R.string.form_error_save_storage_full
                        is SaveError.Unknown -> R.string.form_error_save_unknown
                    }
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(resourcesId)
                    )
                }
            }
        }
    }

    EditCandidateScreen(
        editUiState = editUiState,
        onBackClick = onBackClick,
        onSaveSubmit = viewModel::saveCandidate,
        onDateOfBirthChange = viewModel::onDateOfBirthChange,
        onPhotoChange = viewModel::onPhotoChange,
        snackbarHostState = snackbarHostState
    )
}
