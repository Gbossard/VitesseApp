package com.example.core.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.executeSQL
import androidx.room3.useWriterConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class CandidateDaoTest {

    private lateinit var candidateDao: CandidateDao
    private lateinit var db: CandidateDatabase
    private lateinit var result: Flow<List<CandidateEntity>>
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder<CandidateDatabase>(context)
            .setDriver(BundledSQLiteDriver())
            .build()
        candidateDao = db.candidateDao()
    }

    private suspend fun insertCandidate() {
        db.useWriterConnection { connection ->
            connection.executeSQL(
                """
                    INSERT INTO candidate_table 
                    ( id, firstName, lastName, phone, email, dateOfBirth, photo, salary, notes, isFavorite) 
                        VALUES 
                            ('1', 'Jean', 'Dupont', '0601020304', 'jean.dupont@gmail.com', '1992-06-20', NULL, 45000, 'Available now', 1),
                            ('2', 'Robert', 'Dupont', '0602030405', 'robert.dupont@gmail.com', '1993-01-26', NULL, 50000, 'Available in 3 months', 0),
                            ('3', 'Michel', 'Martin', '0645789562', 'michel.martin@gmail.com', '1956-08-23', NULL, 90000, 'Available in 1 week', 1)
 
                """
            )
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    // getAllCandidates
    @Test
    fun getAllCandidates_whenDatabaseIsEmpty_returnsEmptyList() = runTest {
        result = candidateDao.getAllCandidates("")
        assertTrue(result.first().isEmpty())
    }

    @Test
    fun getAllCandidates_whenQueryIsEmpty_returnsAllCandidates() = runTest {
        insertCandidate()
        result = candidateDao.getAllCandidates("")
        assertEquals(3, result.first().size)
    }

    @Test
    fun getAllCandidates_whenQueryIsNotEmpty_returnsFilteredCandidates() = runTest {
        insertCandidate()
        result = candidateDao.getAllCandidates("Jean")
        assertEquals(1, result.first().size)
    }

    // getAllFavorites
    @Test
    fun getAllFavorites_whenDatabaseIsEmpty_returnsEmptyList() = runTest {
        result = candidateDao.getAllFavorites("")
        assertTrue(result.first().isEmpty())
    }

    @Test
    fun getAllFavorites_whenQueryIsEmpty_returnsAllFavorites() = runTest {
        insertCandidate()
        result = candidateDao.getAllFavorites("")
        assertEquals(2, result.first().size)
    }

    @Test
    fun getAllFavorites_whenQueryIsNotEmpty_returnsFilteredFavorites() = runTest {
        insertCandidate()
        result = candidateDao.getAllFavorites("Dupont")
        assertEquals(1, result.first().size)
    }

    // upsertCandidate
    @Test
    fun upsertCandidate_addCandidate() = runTest {
        val candidate = CandidateEntity(
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
        )
        result = candidateDao.getAllCandidates("")
        assertEquals(0, result.first().size)

        candidateDao.upsertCandidate(candidate)
        assertEquals(1, result.first().size)
    }

    @Test
    fun upsertCandidate_updateCandidate() = runTest {
        val initialCandidate = CandidateEntity(
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
        )
        candidateDao.upsertCandidate(initialCandidate)

        result = candidateDao.getAllCandidates("")
        assertEquals(1, result.first().size)

        val updatedCandidate = initialCandidate.copy(
            firstName = "Fake first name update",
            lastName = "Fake last name update",
            notes = "fake note update",
            isFavorite = true
        )
        candidateDao.upsertCandidate(updatedCandidate)

        assertEquals(1, result.first().size)
        assertEquals(updatedCandidate, result.first().first())
    }
}