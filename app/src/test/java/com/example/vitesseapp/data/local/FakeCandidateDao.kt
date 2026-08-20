package com.example.vitesseapp.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeCandidateDao : CandidateDao {
    override fun getAllCandidates(query: String): Flow<List<CandidateEntity>> {
        return flowOf(
            listOf(
                CandidateEntity(
                    id = "1",
                    firstName = "Fake first name",
                    lastName = "Fake last name",
                    phone = "0606060606",
                    email = "fake.email@fake.com",
                    dateOfBirth = LocalDate.parse("1992-06-20"),
                    photo = null,
                    salary = 0,
                    notes = "fake note",
                    isFavorite = false
                ),
                CandidateEntity(
                    id = "2",
                    firstName = "Fake first name 2",
                    lastName = "Fake last name 2",
                    phone = "0606060606",
                    email = "fake2.email@fake.com",
                    dateOfBirth = LocalDate.parse("1999-06-20"),
                    photo = null,
                    salary = 0,
                    notes = "fake note 2",
                    isFavorite = true
                ),
                CandidateEntity(
                    id = "3",
                    firstName = "Fake first name 3",
                    lastName = "Fake last name 3",
                    phone = "0606060606",
                    email = "fake3.email@fake.com",
                    dateOfBirth = LocalDate.parse("2000-06-20"),
                    photo = null,
                    salary = 0,
                    notes = "fake note 3",
                    isFavorite = true
                )
            )
        )
    }

    override fun getAllFavorites(query: String): Flow<List<CandidateEntity>> {
        return flowOf(
            listOf(
                CandidateEntity(
                    id = "2",
                    firstName = "Fake first name 2",
                    lastName = "Fake last name 2",
                    phone = "0606060606",
                    email = "fake2.email@fake.com",
                    dateOfBirth = LocalDate.parse("1999-06-20"),
                    photo = null,
                    salary = 0,
                    notes = "fake note 2",
                    isFavorite = true
                ),
                CandidateEntity(
                    id = "3",
                    firstName = "Fake first name 3",
                    lastName = "Fake last name 3",
                    phone = "0606060606",
                    email = "fake3.email@fake.com",
                    dateOfBirth = LocalDate.parse("2000-06-20"),
                    photo = null,
                    salary = 0,
                    notes = "fake note 3",
                    isFavorite = true
                )
            )
        )
    }
}