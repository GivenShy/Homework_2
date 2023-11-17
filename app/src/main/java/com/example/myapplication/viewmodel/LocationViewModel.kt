package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.repository.WeatherRepository
import com.example.myapplication.service.LatandLong
import com.example.myapplication.service.LocationService
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _liveDataWeather = MutableLiveData<WeatherResponse>()
    private val _locationService:LocationService = LocationService()
    val liveDataWeather: LiveData<WeatherResponse> = _liveDataWeather
    fun loadTemp(apiKey: String,context: Context):WeatherResponse? {
        var res:WeatherResponse? = null
        try{
        viewModelScope.launch {
            var temp:LatandLong? = _locationService.getUserLocation(context = context)
            if(temp!=null) {
                var coordinates = temp!!
                var address = _locationService.getReadableLocation(
                    coordinates.latitude,
                    coordinates.longitude,
                    context
                )
                res = repository.loadWeather(address, apiKey)
                var k:String?=null

                _liveDataWeather.postValue(res);
            }
        }
        }
        catch (e: Exception) {
            // Handle exceptions
            Log.e("CoroutineException", "Exception: ${e.message}")
            // Notify or log the error message
        }
        return res
    }

}