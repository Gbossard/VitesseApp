package com.example.vitesseapp.di

import android.content.Context
import com.example.vitesseapp.data.local.CandidateDao
import com.example.vitesseapp.data.local.CandidateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCandidateDatabase(@ApplicationContext context: Context) : CandidateDatabase {
        return CandidateDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideCandidateDao(database: CandidateDatabase) : CandidateDao {
        return database.candidateDao()
    }
}