package com.example.vitesseapp.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "candidate_table")
data class CandidateEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
)