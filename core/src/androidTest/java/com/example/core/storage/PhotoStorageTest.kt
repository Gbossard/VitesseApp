package com.example.core.storage

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.example.core.data.storage.PhotoStorage
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

class PhotoStorageTest {
    private lateinit var context: Context
    private lateinit var photoStorage: PhotoStorage

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        photoStorage = PhotoStorage(context)
    }

    @Test
    fun copyPhoto_returnsSuccess() = runTest {
        val file = File(context.cacheDir, "uri.jpg")
        file.writeText("Fake text")
        val uri = Uri.fromFile(file)

        val result = photoStorage.copyPhoto(uri)

        assertTrue(result.isSuccess)

        val savedUriString = result.getOrThrow()
        val savedFile = File(Uri.parse(savedUriString).path!!)

        assertEquals("Fake text", savedFile.readText())
        file.delete()
        savedFile.delete()
    }

    @Test
    fun copyPhoto_returnsFileNotFoundError() = runTest {
        val file = File(context.filesDir, "fileNotFound.jpg")
        val uri = Uri.fromFile(file)

        val result = photoStorage.copyPhoto(uri)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is FileNotFoundException)
    }

    @Test
    fun copyPhoto_returnsError() = runTest {
        val uri = Uri.parse("invalidUri://path")

        val result = photoStorage.copyPhoto(uri)

        assertTrue(result.isFailure)
    }
}