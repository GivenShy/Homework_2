package com.example.myapplication.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.utils.Constants
import com.example.myapplication.viewmodel.CityExplorerViewModel
import com.example.myapplication.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import retrofit2.Response


enum class LoadingState { Idle, Loading, Loaded, Error }

@Composable
fun SecondScreen(navController: NavController, context: Context, viewModel: CityExplorerViewModel = viewModel(), settingsViewModel: SettingsViewModel = viewModel()) {


    val cityWeather = viewModel.liveDataWeather.observeAsState()
    val cities = viewModel.getCities();
    var callsAreMade = false

    if (cityWeather.value?.size !=cities.size && !callsAreMade) {
        for (city in cities) {
            fetchCity(viewModel, city.name)
        }
        callsAreMade = true
    }

    // Create a mutable state to hold the loading state
    var loadingState by remember { mutableStateOf(LoadingState.Loaded) }

    LaunchedEffect(Unit) {
        // Set the loading state to "Loading"
        loadingState = LoadingState.Loading

        var success = false
        if(cityWeather.value?.size !=cities.size){
            delay(3500)
            success = cityWeather.value?.size==cities.size
        }


        loadingState = if (success) LoadingState.Loaded else LoadingState.Error
    }

    // Display different UI based on loading state
    when (loadingState) {
        LoadingState.Idle -> {
            // Initial state (optional)
        }

        LoadingState.Loading -> {
            // Show a loading indicator or progress bar
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LoadingState.Loaded -> {
            Column {
                Text(
                    text = "Cities",
                    fontSize = 21.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                Column {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false),
                    ) {
                        for (city in cities) {
                            var response: Response<WeatherResponse>? = null
                            cityWeather.value?.let { cities ->
                                response = cities[city.name]
                            }
                            CityView(city = city, weatherResponse = response?.body(), temperatureUnit = settingsViewModel.getTemperatureUnitPreference(context = context))
                        }
                    }
                    Button(
                        onClick = { navController.navigate("welcome_screen") },
                        modifier = Modifier
                            .paddingFromBaseline(0.dp, 30.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Back")
                    }
                }
            }
        }

        else -> {
            Column {
                Text(text = "Something went wrong")
                Button(onClick = {navController.navigate("welcome_screen")}) {
                    Text(text = "Home")
                }
            }

        }
    }

}

fun fetchCity(viewModel: CityExplorerViewModel, cityName: String) {
    viewModel.loadWeather(cityName, Constants.apiKey)
}