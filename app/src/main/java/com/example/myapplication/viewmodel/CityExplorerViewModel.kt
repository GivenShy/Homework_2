package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.repository.WeatherRepository
import kotlinx.coroutines.launch
import retrofit2.Response

data class City(
    val name: String,
    val description: Int,
    val imageUrl: Int
)
class CityExplorerViewModel : ViewModel() {

    private val repository = WeatherRepository()
    private val _liveDataWeather = MutableLiveData<HashMap<String, Response<WeatherResponse>>>()
    val liveDataWeather: LiveData<HashMap<String, Response<WeatherResponse>>> = _liveDataWeather

    init {
        _liveDataWeather.value = HashMap<String,Response<WeatherResponse>>()
    }
    private val cities = listOf(
        City("Yerevan", R.string.yerevan, R.drawable.yerevan),
        City("Washington", R.string.washington,R.drawable.washington ),
        City("Madrid", R.string.madrid,R.drawable.madrid),
        City("Paris", R.string.paris,R.drawable.paris)
    )
    fun loadWeather(q: String, apiKey: String) {
        try {

            viewModelScope.launch {
                val res = repository.loadWeather(q, apiKey)
                var newMap = HashMap<String, Response<WeatherResponse>>()
                if (_liveDataWeather.value != null) {
                    newMap = _liveDataWeather.value?.let { HashMap(it) }!!
                }

                newMap.set(q, res)
                _liveDataWeather.postValue(newMap)
            }
        //}
        }catch (ex:Exception){
            Log.e("CoroutineException", "Exception: ${ex.message}")
        }
    }



    fun getCities(): List<City> {
        return cities
    }


}
