package com.example.core.data.repository

import com.example.core.data.local.CandidateDao
import com.example.core.data.local.CandidateEntity
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class CandidateRepositoryImplTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var candidateDao: CandidateDao
    private lateinit var candidateRepositoryImpl: CandidateRepositoryImpl

    @Before
    fun setUp() {
        candidateRepositoryImpl = CandidateRepositoryImpl(candidateDao)
    }

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
}