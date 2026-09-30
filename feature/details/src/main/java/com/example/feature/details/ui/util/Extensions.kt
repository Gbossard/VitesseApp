package com.example.feature.details.ui.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.net.toUri
import com.example.feature.details.R

fun Context.dialPhoneNumber(phoneNumber: String, onError: (String) -> Unit) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    startActivitySafe(
        intent = intent,
        errorResId = R.string.call_no_apps_error,
        onError = onError
    )
}

fun Context.startActivitySafe(
    intent: Intent,
    @StringRes errorResId: Int,
    onError: (String) -> Unit
) {
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        onError(getString(errorResId))
    }
}