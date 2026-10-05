package com.example.core.data.repository

import com.example.core.data.local.CandidateDao
import com.example.core.data.local.CandidateEntity
import com.example.core.data.network.CurrencyApiService
import com.example.core.data.network.CurrencyResponseData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class CandidateRepositoryImplTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var candidateDao: CandidateDao

    @MockK
    lateinit var currencyApiService: CurrencyApiService
    private lateinit var candidateRepositoryImpl: CandidateRepositoryImpl

    @Before
    fun setUp() {
        candidateRepositoryImpl = CandidateRepositoryImpl(candidateDao, currencyApiService)
    }

    private val candidate = CandidateEntity(
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

    // getAllCandidates
    @Test
    fun getAllCandidates_returnsDataFromDao() = runTest {
        val listOfCandidates = listOf(
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
            ),
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
        every { candidateDao.getAllCandidates("") } returns flowOf(listOfCandidates)
        assertEquals(listOfCandidates, candidateRepositoryImpl.getAllCandidates("").first())
    }

    // getAllFavorites
    @Test
    fun getAllFavorites_returnsDataFromDao() = runTest {
        val listOfFavorites = listOf(
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
        every { candidateDao.getAllFavorites("") } returns flowOf(listOfFavorites)
        assertEquals(listOfFavorites, candidateRepositoryImpl.getAllFavorites("").first())
    }

    // toggleFavorite
    @Test
    fun toggleFavorite_returnsDataFromDao() = runTest {
        val candidate = CandidateEntity(
            id = "1",
            firstName = "Fake first name",
            lastName = "Fake last name",
            phone = "0606060606",
            email = "fake.email@fake.com",
            dateOfBirth = LocalDate.parse("1992-06-20"),
            photo = null,
            salary = 0,
            notes = "fake note",
            isFavorite = true
        )
        coEvery { candidateDao.toggleFavorite(candidate.id) } just runs
        candidateRepositoryImpl.toggleFavorite(candidate.id)
        coVerify(exactly = 1) { candidateDao.toggleFavorite(candidate.id) }
    }

    // getCandidateById
    @Test
    fun getCandidateById_returnsDataFromDao() = runTest {
        coEvery { candidateDao.getCandidateById("1") } returns candidate
        assertEquals(candidate, candidateRepositoryImpl.getCandidateById("1"))
        coVerify(exactly = 1) { candidateDao.getCandidateById("1") }
    }

    // getCandidateByIdFlow
    @Test
    fun getCandidateByIdFlow_returnsDataFromDao() = runTest {
        every { candidateDao.getCandidateByIdFlow("1") } returns flowOf(candidate)
        assertEquals(candidate, candidateRepositoryImpl.getCandidateByIdFlow("1").first())
    }

    // upsertCandidate
    @Test
    fun upsertCandidate_saveCandidateToDao() = runTest {
        coEvery { candidateDao.upsertCandidate(candidate) } just runs

        candidateRepositoryImpl.upsertCandidate(candidate)

        coVerify(exactly = 1) { candidateDao.upsertCandidate(candidate) }
    }

    // deleteCandidate
    @Test
    fun deleteCandidate() = runTest {
        coEvery { candidateDao.deleteCandidate(candidate) } just runs

        candidateRepositoryImpl.deleteCandidate(candidate)
        coVerify(exactly = 1) { candidateDao.deleteCandidate(candidate)}
    }

    // convertSalaryToGbp
    @Test
    fun convertSalaryToGbp_returnsSuccess() = runTest {
        val salaryInEuros = 50000
        val currency = CurrencyResponseData(
            date = "2026-10-05",
            rates = mapOf("gbp" to 0.85)
        )
        coEvery { currencyApiService.getEuroExchangeRates() } returns currency

        val result = candidateRepositoryImpl.convertSalaryToGbp(salaryInEuros)

        assertTrue(result.isSuccess)
        assertEquals(42500.0, result.getOrNull()!!, 0.01)
        coVerify(exactly = 1) { currencyApiService.getEuroExchangeRates() }
    }

    @Test
    fun convertSalaryToGbp_returnsFailureWithNoSuchElementException() = runTest {
        val salaryInEuros = 50000
        val currency = CurrencyResponseData(
            date = "2026-10-05",
            rates = mapOf("usd" to 1.1)
        )
        coEvery { currencyApiService.getEuroExchangeRates() } returns currency

        val result = candidateRepositoryImpl.convertSalaryToGbp(salaryInEuros)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

    @Test
    fun convertSalaryToGbp_returnsFailure() = runTest {
        val salaryInEuros = 50000
        coEvery { currencyApiService.getEuroExchangeRates() } throws Exception("Network error")

        val result = candidateRepositoryImpl.convertSalaryToGbp(salaryInEuros)
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}