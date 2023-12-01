package com.example.myapplication.service

import com.example.myapplication.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/current.json")
    suspend fun fetchWeather(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Response<WeatherResponse>
}