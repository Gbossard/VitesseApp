package com.example.vitesseapp.data.local

import androidx.room3.Dao
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidate_table")
    fun getAllCandidates(): Flow<List<CandidateEntity>>

    @Query("SELECT * FROM candidate_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<CandidateEntity>>
}