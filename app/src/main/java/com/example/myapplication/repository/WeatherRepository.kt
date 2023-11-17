package com.example.myapplication.repository

import com.example.myapplication.service.WeatherApiService
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.retrofit.RetrofitConfig

class WeatherRepository {
    suspend fun loadWeather(q: String, apiKey: String): WeatherResponse {
        val apiService = RetrofitConfig.getRetrofit().create(WeatherApiService::class.java)
        return apiService.fetchWeather(
            q,
            apiKey,
        )
    }
}