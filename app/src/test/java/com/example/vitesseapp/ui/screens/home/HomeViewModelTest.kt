package com.example.vitesseapp.ui.screens.home

import com.example.vitesseapp.data.local.CandidateEntity
import com.example.vitesseapp.data.repository.FakeCandidateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeCandidateRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = FakeCandidateRepository()
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
        repository.candidatesFlow = flowOf(candidates)

        val states = mutableListOf<HomeUiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.candidatesUiState.collect { states.add(it) }
        }

        advanceTimeBy(301.milliseconds)
        assertEquals(HomeUiState.Loading, states[0])
        assertEquals(HomeUiState.Success(candidates), states[1])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCandidatesUiState_returnsEmptyState() = runTest {
        repository.candidatesFlow = flowOf(emptyList())
        val states = mutableListOf<HomeUiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.candidatesUiState.collect { states.add(it) }
        }

        advanceTimeBy(301.milliseconds)
        assertEquals(HomeUiState.Loading, states[0])
        assertEquals(HomeUiState.Empty, states[1])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCandidatesUiState_returnsErrorState() = runTest {
        repository.candidatesFlow = flow {
            throw RuntimeException("Error")
        }

        val states = mutableListOf<HomeUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.candidatesUiState.collect { states.add(it) }
        }

        advanceTimeBy(301.milliseconds)
        assertEquals(HomeUiState.Loading, states[0])
        assertEquals(HomeUiState.Error, states[1])
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
        repository.favoritesFlow = flowOf(favorites)

        val states = mutableListOf<HomeUiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.favoritesUiState.collect { states.add(it) }
        }

        advanceTimeBy(301.milliseconds)
        assertEquals(HomeUiState.Loading, states[0])
        assertEquals(HomeUiState.Success(favorites), states[1])
    }

    @Test
    fun onClearQuery_queryIsEmpty() {
        viewModel.onQueryChange("Jean")

        viewModel.onClearQuery()
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun onQueryChange_updatedQuery() {
        viewModel.onQueryChange("Jean")
        assertEquals("Jean", viewModel.searchQuery.value)
    }

}