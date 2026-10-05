package com.example.core.data.network

import retrofit2.http.GET

interface CurrencyApiService {
    @GET("currencies/eur.json")
    suspend fun getEuroExchangeRates(): CurrencyResponseData
}