package com.example.feature.details.ui.screens

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.core.data.local.CandidateEntity
import com.example.core.data.repository.CandidateRepository
import com.example.core.testing.MainDispatcherRule
import com.example.feature.details.ui.DetailsCandidateUiState
import com.example.feature.details.ui.DetailsCandidateViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsCandidateViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: CandidateRepository

    private val candidateId = "1"
    private val candidate = CandidateEntity(
        id = candidateId,
        firstName = "Fake first name",
        lastName = "Fake last name",
        phone = "0606060606",
        email = "fake.email@fake.com",
        dateOfBirth = LocalDate.parse("1992-06-20"),
        photo = null,
        salary = 0,
        notes = "fake note",
        isFavorite = false
    )

    private fun createViewModel(): DetailsCandidateViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("candidateId" to candidateId))
        return DetailsCandidateViewModel(
            savedStateHandle = savedStateHandle,
            candidateRepository = repository
        )
    }

    @Test
    fun getUiState_returnsSuccessState() = runTest {
        every { repository.getCandidateByIdFlow(candidateId) } returns flowOf(candidate)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(DetailsCandidateUiState.Loading, awaitItem())
            assertEquals(DetailsCandidateUiState.Success(candidate), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getUiState_returnsErrorState() = runTest {
        every { repository.getCandidateByIdFlow(candidateId) } returns flow {
            throw RuntimeException("Error")
        }

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(DetailsCandidateUiState.Loading, awaitItem())
            assertEquals(DetailsCandidateUiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun toggleFavorite_callsRepositoryFavorite() = runTest {
        every { repository.getCandidateByIdFlow(candidateId) } returns flowOf(candidate)
        coEvery { repository.toggleFavorite(candidateId) } just runs

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(DetailsCandidateUiState.Loading, awaitItem())
            assertEquals(DetailsCandidateUiState.Success(candidate), awaitItem())
            viewModel.toggleFavorite()
            advanceUntilIdle()
            coVerify(exactly = 1) { repository.toggleFavorite(candidateId)}
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteCandidate_callsRepositoryDelete() = runTest {
        every { repository.getCandidateByIdFlow(candidateId) } returns flowOf(candidate)
        coEvery { repository.deleteCandidate(candidate) } just runs

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(DetailsCandidateUiState.Loading, awaitItem())
            assertEquals(DetailsCandidateUiState.Success(candidate), awaitItem())
            viewModel.deleteCandidate(candidate)
            advanceUntilIdle()
            coVerify(exactly = 1) { repository.deleteCandidate(candidate) }
            cancelAndIgnoreRemainingEvents()
        }
    }

}