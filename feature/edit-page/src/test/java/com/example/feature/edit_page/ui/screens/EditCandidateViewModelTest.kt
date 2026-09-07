package com.example.feature.edit_page.ui.screens

import android.net.Uri
import app.cash.turbine.test
import com.example.core.data.repository.CandidateRepository
import com.example.core.data.storage.PhotoStorage
import com.example.core.testing.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDate

class EditCandidateViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: CandidateRepository

    @MockK
    lateinit var photoStorage: PhotoStorage

    private lateinit var viewModel: EditCandidateViewModel

    @Before
    fun setUp() {
        viewModel = EditCandidateViewModel(repository, photoStorage)
    }

    @Test
    fun saveCandidate_withNewPhoto_returnsSuccessState() = runTest {
        val uri = mockk<Uri> {
            every { scheme } returns "content"
        }

        val savedUri = "file://candidate_123"
        viewModel.onPhotoChange(uri)

        coEvery { photoStorage.copyPhoto(uri) } returns Result.success(savedUri)
        coEvery { repository.upsertCandidate(any()) } just Runs

        viewModel.events.test {
            viewModel.saveCandidate()
            assertEquals(EditUiEvent.SaveSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { photoStorage.copyPhoto(uri) }
        coVerify { repository.upsertCandidate(match { it.photo == savedUri }) }
    }

    @Test
    fun saveCandidate_withExistingPhoto_returnsSuccessState() = runTest {
        val uri = mockk<Uri> {
            every { scheme } returns "file"
        }
        viewModel.onPhotoChange(uri)

        coEvery { repository.upsertCandidate(any()) } just Runs

        viewModel.events.test {
            viewModel.saveCandidate()
            assertEquals(EditUiEvent.SaveSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repository.upsertCandidate(match { it.photo == uri.toString() }) }
        coVerify(exactly = 0) { photoStorage.copyPhoto(any()) }
    }

    @Test
    fun saveCandidate_withoutPhoto_returnsSuccessState() = runTest {
        coEvery { repository.upsertCandidate(any()) } just Runs

        viewModel.events.test {
            viewModel.saveCandidate()
            assertEquals(EditUiEvent.SaveSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repository.upsertCandidate(match { it.photo == null }) }
    }

    @Test
    fun saveCandidate_withNewPhoto_returnsFileNotFoundErrorState() = runTest {
        val uri = mockk<Uri> {
            every { scheme } returns "content"
        }
        viewModel.onPhotoChange(uri)

        coEvery { photoStorage.copyPhoto(uri) } returns Result.failure(FileNotFoundException())

        viewModel.events.test {
            viewModel.saveCandidate()
            assertEquals(EditUiEvent.SaveErrorEvent(SaveError.FileNotFound), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { photoStorage.copyPhoto(uri) }
        coVerify(exactly = 0) { repository.upsertCandidate(any()) }
    }

    @Test
    fun saveCandidate_withNewPhoto_returnsStorageFullErrorState() = runTest {
        val uri = mockk<Uri> {
            every { scheme } returns "content"
        }
        viewModel.onPhotoChange(uri)

        coEvery { photoStorage.copyPhoto(uri) } returns Result.failure(IOException())

        viewModel.events.test {
            viewModel.saveCandidate()
            assertEquals(EditUiEvent.SaveErrorEvent(SaveError.StorageFull), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { photoStorage.copyPhoto(uri) }
        coVerify(exactly = 0) { repository.upsertCandidate(any()) }
    }

    @Test
    fun saveCandidate_withNewPhoto_returnsUnknownErrorState() = runTest {
        val uri = mockk<Uri> {
            every { scheme } returns "content"
        }
        viewModel.onPhotoChange(uri)

        coEvery { photoStorage.copyPhoto(uri) } returns Result.failure(Exception())

        viewModel.events.test {
            viewModel.saveCandidate()
            assertEquals(EditUiEvent.SaveErrorEvent(SaveError.Unknown), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { photoStorage.copyPhoto(uri) }
        coVerify(exactly = 0) { repository.upsertCandidate(any()) }
    }

    @Test
    fun saveCandidate_returnsDatabaseErrorState() = runTest {
        coEvery { repository.upsertCandidate(any()) } throws Exception("Error")

        viewModel.events.test {
            viewModel.saveCandidate()
            assertEquals(EditUiEvent.SaveErrorEvent(SaveError.DatabaseError), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onDateOfBirthChange_updatedDateOfBirth() {
        viewModel.onDateOfBirthChange(LocalDate.of(2007,6,25))
        assertEquals(LocalDate.of(2007,6,25), viewModel.editUiState.value.dateOfBirth)
    }

    @Test
    fun onPhotoChange_updatedPhoto() {
        val uri = mockk<Uri>()
        viewModel.onPhotoChange(uri)
        assertEquals(uri, viewModel.editUiState.value.photo)
    }

}