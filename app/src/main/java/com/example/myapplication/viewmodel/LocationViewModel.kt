package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.repository.WeatherRepository
import com.example.myapplication.service.LocationService
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _liveDataWeather = MutableLiveData<HashMap<String, WeatherResponse>>()
    private val _locationService:LocationService = LocationService()
    fun loadTemp(apiKey: String,context: Context) {
        viewModelScope.launch {
            var city = _locationService.getLocation(context)
            val res = repository.loadWeather(city, apiKey)
            var newMap = HashMap<String, WeatherResponse>()
            if (_liveDataWeather.value != null) {
                newMap = _liveDataWeather.value?.let { HashMap(it) }!!
            }

            newMap.set(city, res)
            _liveDataWeather.postValue(newMap)
        }
    }

}