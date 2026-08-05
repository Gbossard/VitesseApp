package com.example.vitesseapp.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(entities = [CandidateEntity::class], version = 1, exportSchema = false)
abstract class CandidateDatabase : RoomDatabase() {

    abstract fun candidateDao(): CandidateDao
    companion object {
        @Volatile
        private var Instance: CandidateDatabase? = null

        fun getDatabase(context: Context): CandidateDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CandidateDatabase::class.java, "candidate_database")
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}