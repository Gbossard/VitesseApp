package com.example.vitesseapp.data.repository

import com.example.vitesseapp.data.local.CandidateDao
import com.example.vitesseapp.data.local.CandidateEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CandidateRepository {
    fun getAllCandidates(query: String): Flow<List<CandidateEntity>>

    fun getAllFavorites(): Flow<List<CandidateEntity>>
}

class CandidateRepositoryImpl @Inject constructor(
    private val dao: CandidateDao
) : CandidateRepository {
    override fun getAllCandidates(query: String): Flow<List<CandidateEntity>> = dao.getAllCandidates(query)

    override fun getAllFavorites(): Flow<List<CandidateEntity>> = dao.getAllFavorites()
}