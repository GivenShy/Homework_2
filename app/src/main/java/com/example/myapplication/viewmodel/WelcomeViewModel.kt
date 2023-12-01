package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.repository.WeatherRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class WelcomeViewModel:ViewModel() {
    private val repository = WeatherRepository()
    private val _liveDataWeather = MutableLiveData< Response<WeatherResponse>>()
    val liveDataWeather: LiveData<Response<WeatherResponse>> = _liveDataWeather
    fun loadWeatherFromCurrentCity(q: String, apiKey: String) {
        try {

            viewModelScope.launch {
                val res = repository.loadWeather(q, apiKey)
                if(res.isSuccessful){
                    _liveDataWeather.postValue(res)
                }
                else{
                    _liveDataWeather.postValue(null)
                }
            }
            //}
        }catch (ex:Exception){
            Log.e("CoroutineException", "Exception: ${ex.message}")
        }
    }
}