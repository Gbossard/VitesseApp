package com.example.core.data.storage

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID
import javax.inject.Inject

class PhotoStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun copyPhoto(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?:
            return@withContext Result.failure(FileNotFoundException())

            val fileName = UUID.randomUUID().toString()
            inputStream.use { input ->
                context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                    input.copyTo(output)
                }
            }

            val savedFile = File(context.filesDir, fileName)
            val localUri = Uri.fromFile(savedFile).toString()
            Result.success(localUri)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}