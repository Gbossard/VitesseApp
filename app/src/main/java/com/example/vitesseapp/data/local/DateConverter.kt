package com.example.vitesseapp.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room3.ColumnTypeConverter
import java.time.LocalDate

class DateConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnTypeConverter
    fun fromString(value: String): LocalDate {
        return value.let { LocalDate.parse(it) }
    }

    @ColumnTypeConverter
    fun toString(date: LocalDate): String {
        return date.toString()
    }
}