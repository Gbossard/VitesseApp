package com.example.feature.edit_page.ui.screens

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.local.CandidateEntity
import com.example.core.data.repository.CandidateRepository
import com.example.core.data.storage.PhotoStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class EditUiState(
    val firstName: TextFieldState = TextFieldState(),
    val lastName: TextFieldState = TextFieldState(),
    val phone: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val dateOfBirth: LocalDate? = null,
    val photo: Uri? = null,
    val salary: TextFieldState = TextFieldState(),
    val notes: TextFieldState = TextFieldState(),
    val isFavorite: Boolean = false
)

sealed interface EditUiEvent {
    data object SaveSuccess: EditUiEvent
    data class SaveErrorEvent(val error: SaveError): EditUiEvent
}

sealed interface SaveError {
    data object DatabaseError: SaveError
    data object FileNotFound: SaveError
    data object StorageFull: SaveError
    data object Unknown: SaveError
}

@HiltViewModel
class EditCandidateViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository,
    private val photoStorage: PhotoStorage
): ViewModel() {

    private val _editUiState = MutableStateFlow(EditUiState())
    val editUiState: StateFlow<EditUiState> = _editUiState.asStateFlow()

    private val _events = Channel<EditUiEvent>()
    val events = _events.receiveAsFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveCandidate() {
        viewModelScope.launch {
            val currentState = _editUiState.value
            var finalPhoto: String? = null

            if (currentState.photo != null) {
                val tempUri = currentState.photo

                if (tempUri.scheme == "file") {
                    finalPhoto = tempUri.toString()
                } else {
                    photoStorage.copyPhoto(tempUri)
                        .onSuccess {
                            finalPhoto = it
                        }
                        .onFailure { exception ->
                            val errorType = when(exception) {
                                is FileNotFoundException -> SaveError.FileNotFound
                                is IOException -> SaveError.StorageFull
                                else -> SaveError.Unknown
                            }
                            _events.send(EditUiEvent.SaveErrorEvent(errorType))
                            return@launch
                        }
                }

            }

            val candidate = CandidateEntity(
                id = UUID.randomUUID().toString(),
                firstName = currentState.firstName.text.toString(),
                lastName = currentState.lastName.text.toString(),
                phone = currentState.phone.text.toString(),
                email = currentState.email.text.toString(),
                dateOfBirth = currentState.dateOfBirth ?: LocalDate.now(),
                photo = finalPhoto,
                salary = currentState.salary.text.toString().toIntOrNull() ?: 0,
                notes = currentState.notes.text.toString(),
                isFavorite = currentState.isFavorite
            )
            try {
                candidateRepository.upsertCandidate(candidate)
                _events.send(EditUiEvent.SaveSuccess)
            } catch (_: Exception) {
                _events.send(EditUiEvent.SaveErrorEvent(SaveError.DatabaseError))
            }
        }
    }

    fun onDateOfBirthChange(newDateOfBirth: LocalDate?) {
        _editUiState.update { it.copy(dateOfBirth = newDateOfBirth) }
    }

    fun onPhotoChange(newUri: Uri?) {
        _editUiState.update { it.copy(photo = newUri) }
    }
}