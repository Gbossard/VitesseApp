package com.example.core.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponseData(
    val date: String? = null,
    @SerialName(value = "eur")
    val rates: Map<String, Double>? = null
)