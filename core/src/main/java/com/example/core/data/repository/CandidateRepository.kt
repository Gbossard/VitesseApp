package com.example.core.data.repository

import com.example.core.data.local.CandidateDao
import com.example.core.data.local.CandidateEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CandidateRepository {
    fun getAllCandidates(query: String): Flow<List<CandidateEntity>>

    fun getAllFavorites(query: String): Flow<List<CandidateEntity>>

    suspend fun upsertCandidate(candidate: CandidateEntity)
}

class CandidateRepositoryImpl @Inject constructor(
    private val dao: CandidateDao
) : CandidateRepository {
    override fun getAllCandidates(query: String): Flow<List<CandidateEntity>> = dao.getAllCandidates(query)

    override fun getAllFavorites(query: String): Flow<List<CandidateEntity>> = dao.getAllFavorites(query)

    override suspend fun upsertCandidate(candidate: CandidateEntity) = dao.upsertCandidate(candidate)
}