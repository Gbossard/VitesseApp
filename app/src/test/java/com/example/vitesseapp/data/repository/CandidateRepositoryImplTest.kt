package com.example.vitesseapp.data.repository

import com.example.vitesseapp.data.local.FakeCandidateDao
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

class CandidateRepositoryImplTest {
    private lateinit var candidateDao: FakeCandidateDao
    private lateinit var candidateRepositoryImpl: CandidateRepositoryImpl

    @Before
    fun setUp() {
        candidateDao = FakeCandidateDao()
        candidateRepositoryImpl = CandidateRepositoryImpl(candidateDao)
    }

    @Test
    fun getAllCandidates_returnsDataFromDao() = runTest {
        assertEquals(candidateDao.getAllCandidates("").first(), candidateRepositoryImpl.getAllCandidates("").first())
    }

    @Test
    fun getAllFavorites_returnsDataFromDao() = runTest {
        assertEquals(candidateDao.getAllFavorites("").first(), candidateRepositoryImpl.getAllFavorites("").first())
    }
}