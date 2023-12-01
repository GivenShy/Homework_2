package com.example.myapplication.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.SettingsViewModel
import com.example.myapplication.viewmodel.TemperatureUnit


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsPage(navController: NavController, context: Context, settingsViewModel: SettingsViewModel = viewModel()) {
    var temperatureUnit by remember { mutableStateOf(settingsViewModel.getTemperatureUnitPreference(context)) }
    //var temperatureUnit by remember { mutableStateOf(TemperatureUnit.CELSIUS) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Temperature Unit Preference", style = MaterialTheme.typography.headlineMedium)

        SwitchItem(
            text = "Celsius (°C)",
            selected = temperatureUnit == TemperatureUnit.CELSIUS,
            onToggle = {
                temperatureUnit = TemperatureUnit.CELSIUS
                settingsViewModel.saveTemperatureUnitPreference(context,TemperatureUnit.CELSIUS)  }
        )

        SwitchItem(
            text = "Fahrenheit (°F)",
            selected = temperatureUnit == TemperatureUnit.FAHRENHEIT,
            onToggle = {
                temperatureUnit = TemperatureUnit.FAHRENHEIT
                settingsViewModel.saveTemperatureUnitPreference(context,TemperatureUnit.FAHRENHEIT)
            }
        )

        // Display the selected unit
        Text(
            text = "Selected Unit: ${if (temperatureUnit== TemperatureUnit.CELSIUS) "Celsius (°C)" else "Fahrenheit (°F)"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = {navController.navigate("welcome_screen")
        settingsViewModel.setTemperatureUnit(temperatureUnit)}) {
            Text(text = "Home")
        }
    }
}

@Composable
fun SwitchItem(
    text: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Switch(
            checked = selected,
            onCheckedChange = { onToggle.invoke() },
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = text)
    }
}