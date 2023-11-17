package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("current") val temp: TemperatureInfo?
)
data class TemperatureInfo(
    @SerializedName("temp_c") val degreesC: String?,
    @SerializedName("temp_f") val degreesF: String?
)