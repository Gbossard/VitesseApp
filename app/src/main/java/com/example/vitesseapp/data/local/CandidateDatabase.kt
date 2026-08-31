package com.example.vitesseapp.data.local

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.core.data.local.CandidateDao
import com.example.core.data.local.CandidateEntity

@Database(entities = [CandidateEntity::class], version = 1, exportSchema = false)
@ColumnTypeConverters(DateConverter::class)
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