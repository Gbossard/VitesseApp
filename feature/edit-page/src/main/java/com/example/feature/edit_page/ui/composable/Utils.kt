package com.example.feature.edit_page.ui.composable

import android.os.Build
import android.os.ext.SdkExtensions
import androidx.annotation.RequiresApi
import com.example.feature.edit_page.R
import com.example.feature.edit_page.ui.screens.FieldError
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun isEmbeddedPhotoPickerSupported(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
        SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 15
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDate(): LocalDate {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault()
    ).toLocalDate()
}

fun FieldError.toErrorMessageRes(): Int = when (this) {
    FieldError.EmptyField -> R.string.form_mandatory_field
    FieldError.InvalidField -> R.string.form_invalid_format
}