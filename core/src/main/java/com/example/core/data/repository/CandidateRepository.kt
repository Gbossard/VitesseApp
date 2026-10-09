package com.example.core.data.repository

import com.example.core.data.local.CandidateDao
import com.example.core.data.local.CandidateEntity
import com.example.core.data.network.CurrencyApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CandidateRepository {
    fun getAllCandidates(query: String): Flow<List<CandidateEntity>>

    fun getAllFavorites(query: String): Flow<List<CandidateEntity>>

    suspend fun toggleFavorite(candidateId: String)

    suspend fun getCandidateById(candidateId: String): CandidateEntity?

    fun getCandidateByIdFlow(candidateId: String): Flow<CandidateEntity>

    suspend fun upsertCandidate(candidate: CandidateEntity)

    suspend fun deleteCandidate(candidate: CandidateEntity)

    suspend fun convertSalaryToGbp(salary: Int): Result<Double>
}

class CandidateRepositoryImpl @Inject constructor(
    private val dao: CandidateDao,
    private val currencyApi: CurrencyApiService
) : CandidateRepository {
    override fun getAllCandidates(query: String): Flow<List<CandidateEntity>> = dao.getAllCandidates(query)

    override fun getAllFavorites(query: String): Flow<List<CandidateEntity>> = dao.getAllFavorites(query)

    override suspend fun toggleFavorite(candidateId: String) = dao.toggleFavorite(candidateId)

    override suspend fun getCandidateById(candidateId: String): CandidateEntity? = dao.getCandidateById(candidateId)

    override fun getCandidateByIdFlow(candidateId: String): Flow<CandidateEntity> = dao.getCandidateByIdFlow(candidateId)

    override suspend fun upsertCandidate(candidate: CandidateEntity) = dao.upsertCandidate(candidate)

    override suspend fun deleteCandidate(candidate: CandidateEntity) = dao.deleteCandidate(candidate)

    override suspend fun convertSalaryToGbp(salary: Int): Result<Double> =
        try {
            val response = currencyApi.getEuroExchangeRates()
            val gbpRates = response.rates?.get("gbp") ?: return Result.failure(
                NoSuchElementException()
            )
            val salaryInGbp = salary * gbpRates
            Result.success(salaryInGbp)
        } catch (e: Exception) {
            Result.failure(e)
        }
}