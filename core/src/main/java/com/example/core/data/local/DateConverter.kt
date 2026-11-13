package com.example.core.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room3.ColumnTypeConverter
import java.time.LocalDate

class DateConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnTypeConverter
    fun fromString(value: String): LocalDate = value.let { LocalDate.parse(it) }

    @ColumnTypeConverter
    fun toString(date: LocalDate): String = date.toString()
}
