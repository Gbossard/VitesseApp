package com.example.vitesseapp.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "candidate_table")
data class CandidateEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val dateOfBirth: LocalDate,
    val photo: String?,
    val salary: Int,
    val notes: String

)