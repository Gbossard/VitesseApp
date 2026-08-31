package com.example.core.di

import com.example.core.data.repository.CandidateRepository
import com.example.core.data.repository.CandidateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCandidateRepository(
        candidateRepositoryImpl: CandidateRepositoryImpl
    ) : CandidateRepository
}