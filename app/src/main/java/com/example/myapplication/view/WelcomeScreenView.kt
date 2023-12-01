package com.example.myapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.utils.Constants
import com.example.myapplication.viewmodel.SettingsViewModel
import com.example.myapplication.viewmodel.TemperatureUnit
import com.example.myapplication.viewmodel.WelcomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

//private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var locationCallback: LocationCallback
private var locationRequired: Boolean = false
private val permissions = arrayOf(
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreenView(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(),
    welcomeViewModel: WelcomeViewModel = viewModel()
) {

    var currentLocation by remember {
        mutableStateOf(LatLng(0.toDouble(), 0.toDouble()))
    }

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            currentLocation, 10f
        )
    }

    var liveWeatherResponse = welcomeViewModel.liveDataWeather.observeAsState()
    var cameraPositionState by remember {
        mutableStateOf(cameraPosition)
    }
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            for (location in p0.locations) {
                currentLocation = LatLng(location.latitude, location.longitude)
                cameraPositionState = CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(
                        currentLocation, 10f
                    )
                )
            }
        }
    }
    val image: Painter = painterResource(id = R.drawable.free_settings_icon_3110_thumb)

    val launchMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val areGranted = permissions.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates(fusedLocationClient)
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Image(
                        painter = image,
                        contentDescription = "Button Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { navController.navigate("settings_screen") }
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = modifier.size(50.dp))
                Text(
                    text = "Welcome to our City Explorer App!",
                    fontSize = 16.sp
                )
                Spacer(modifier = modifier.size(50.dp))
                val marker = LatLng(currentLocation.latitude, currentLocation.longitude)
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    GoogleMap(
                        modifier = Modifier.size(500.dp),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isMyLocationEnabled = true,
                            mapType = MapType.HYBRID,
                            isTrafficEnabled = true
                        )
                    ) {
                        Marker(
                            state = MarkerState(position = marker),
                            title = "MyPosition",
                            snippet = "This is a description of this Marker",
                            draggable = true
                        )
                    }
                } else {
                    launchMultiplePermissions.launch(permissions)
                }
                Button(
                    onClick = {
                        navController.navigate("second_screen")
                    },
                ) {
                    Text(text = "Explore Cities")
                }
                getTempInCurrentCity(currentLocation, welcomeViewModel)
                if (liveWeatherResponse.value != null) {
                    val tempUnit = settingsViewModel.getTemperatureUnitPreference(context)
                    Temperature(
                        tempUnit = tempUnit,
                        weatherResponse = liveWeatherResponse?.value?.body()
                    )
                }

                LocationScreen(context, fusedLocationClient, currentLocation)


            }
        })

}

@Composable
fun Temperature(tempUnit: TemperatureUnit, weatherResponse: WeatherResponse?) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display weather icon (e.g., sunny icon for warm temperature)
        Image(
            painter = painterResource(id = R.drawable.download),
            contentDescription = "Temperature"
        )
        Spacer(modifier = Modifier.size(20.dp))
        var temp: String? = weatherResponse?.temp?.degreesC
        when (tempUnit) {
            TemperatureUnit.CELSIUS -> temp = weatherResponse?.temp?.degreesC
            TemperatureUnit.FAHRENHEIT -> temp = weatherResponse?.temp?.degreesF
        }
        Text(
            text = "$temp",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 4.dp)
        )


        Text(
            text = tempUnit.formatTemperature(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}


@Composable
fun LocationScreen(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    currentLocation: LatLng
) {

    val launchMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val areGranted = permissions.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates(fusedLocationClient)
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Your location: ${currentLocation.latitude}/${currentLocation.longitude}")
            Button(onClick = {
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    startLocationUpdates(fusedLocationClient)
                } else {
                    launchMultiplePermissions.launch(permissions)
                }
            }) {
                Text(text = "Get your location")
            }
        }
    }
}


@SuppressLint("MissingPermission")
fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
    locationCallback?.let {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 100
        )
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3000)
            .setMaxUpdateDelayMillis(100)
            .build()

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}

fun getTempInCurrentCity(coord: LatLng, viewModel: WelcomeViewModel) {
    viewModel.loadWeatherFromCurrentCity("${coord.latitude},${coord.longitude}", Constants.apiKey)
}
