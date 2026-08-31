package com.example.core.data.local

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class DateConverterTest {
    private lateinit var dateConverter: DateConverter
    @Before
    fun setUp() {
        dateConverter = DateConverter()
    }

    @Test
    fun fromString_returnsLocalDate() {
        val result = dateConverter.fromString("2026-01-01")
        assertEquals(LocalDate.of(2026,1,1), result)
    }

    @Test
    fun toString_returnsString() {
        val result = dateConverter.toString(LocalDate.of(2026,1,1))
        assertEquals("2026-01-01", result)
    }

}