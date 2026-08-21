package com.example.vitesseapp.data.repository

import com.example.vitesseapp.data.local.CandidateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCandidateRepository : CandidateRepository {
    var candidatesFlow: Flow<List<CandidateEntity>> = flowOf()
    var favoritesFlow: Flow<List<CandidateEntity>> = flowOf()


    override fun getAllCandidates(query: String): Flow<List<CandidateEntity>> {
        return candidatesFlow
    }


    override fun getAllFavorites(query: String): Flow<List<CandidateEntity>> {
        return favoritesFlow
    }
}