package com.example.feature.home

import app.cash.turbine.test
import com.example.core.data.local.CandidateEntity
import com.example.core.data.repository.CandidateRepository
import com.example.core.testing.MainDispatcherRule
import com.example.feature.home.ui.HomeUiState
import com.example.feature.home.ui.HomeViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class HomeViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: CandidateRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCandidatesUiState_returnsSuccessState() = runTest {
        val candidates = listOf(
            CandidateEntity(
                id = "1",
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
        )
        every { repository.getAllCandidates(any()) } returns flowOf(candidates)

        viewModel.candidatesUiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Success(candidates), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCandidatesUiState_returnsEmptyState() = runTest {
        every { repository.getAllCandidates(any()) } returns flowOf(emptyList())

        viewModel.candidatesUiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Empty, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCandidatesUiState_returnsErrorState() = runTest {
        every { repository.getAllCandidates(any()) } returns flow {
            throw RuntimeException("Error")
        }

        viewModel.candidatesUiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavoritesUiState() = runTest {
        val favorites = listOf(
            CandidateEntity(
                id = "2",
                firstName = "Fake first name 2",
                lastName = "Fake last name 2",
                phone = "0606060606",
                email = "fake2.email@fake.com",
                dateOfBirth = LocalDate.parse("1999-06-20"),
                photo = null,
                salary = 0,
                notes = "fake note 2",
                isFavorite = true
            ),
            CandidateEntity(
                id = "3",
                firstName = "Fake first name 3",
                lastName = "Fake last name 3",
                phone = "0606060606",
                email = "fake3.email@fake.com",
                dateOfBirth = LocalDate.parse("2000-06-20"),
                photo = null,
                salary = 0,
                notes = "fake note 3",
                isFavorite = true
            )
        )
        every { repository.getAllFavorites(any()) } returns flowOf(favorites)

        viewModel.favoritesUiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Success(favorites), awaitItem())
        }
    }

    @Test
    fun onQueryChange_updatedQuery() {
        viewModel.onQueryChange("Jean")
        Assert.assertEquals("Jean", viewModel.searchQuery.value)
    }
}