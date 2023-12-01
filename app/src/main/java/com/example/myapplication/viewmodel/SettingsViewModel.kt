package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class TemperatureUnit(val unitName:String) {
    CELSIUS("°C"),
    FAHRENHEIT("°F");
    fun formatTemperature(): String {
        return "$unitName"
    }
}

class SettingsViewModel : ViewModel() {
    private val _temperatureUnit = MutableStateFlow(TemperatureUnit.CELSIUS)
    val temperatureUnit: StateFlow<TemperatureUnit> = _temperatureUnit

    fun setTemperatureUnit(unit: TemperatureUnit) {
        viewModelScope.launch {
            _temperatureUnit.emit(unit)

        }
    }

    fun saveTemperatureUnitPreference(context: Context, temperatureUnit: TemperatureUnit) {
        val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("temperature_unit", temperatureUnit.name)
        editor.apply()
    }
    fun getTemperatureUnitPreference(context: Context): TemperatureUnit {
        val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedUnit = sharedPreferences.getString("temperature_unit", TemperatureUnit.CELSIUS.name)
        return TemperatureUnit.valueOf(savedUnit ?: TemperatureUnit.CELSIUS.name)
    }
}