package com.example.core.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query(""" 
            SELECT * FROM candidate_table 
            WHERE (firstName || ' ' || lastName) LIKE '%' || :query || '%' 
            OR (lastName || ' ' || firstName) LIKE '%' || :query || '%' 
    """)
    fun getAllCandidates(query: String): Flow<List<CandidateEntity>>

    @Query(""" 
        SELECT * FROM candidate_table WHERE isFavorite = 1
        AND ((firstName || ' ' || lastName) LIKE '%' || :query || '%' 
            OR (lastName || ' ' || firstName) LIKE '%' || :query || '%' )
    """)
    fun getAllFavorites(query: String): Flow<List<CandidateEntity>>

    @Query("UPDATE candidate_table SET isFavorite = NOT isFavorite WHERE id = :candidateId")
    suspend fun toggleFavorite(candidateId: String)

    @Query("SELECT * FROM candidate_table WHERE id = :candidateId")
    suspend fun getCandidateById(candidateId: String): CandidateEntity?

    @Query("SELECT * FROM candidate_table WHERE id = :candidateId")
    fun getCandidateByIdFlow(candidateId: String): Flow<CandidateEntity>

    @Upsert
    suspend fun upsertCandidate(candidate: CandidateEntity)
}