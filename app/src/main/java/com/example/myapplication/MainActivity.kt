package com.example.myapplication

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hw3.utils.PermissionUtils
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.view.SecondScreen
import com.example.myapplication.view.WelcomeScreenView
import com.example.myapplication.view.fetchCity
import com.example.myapplication.viewmodel.CityExplorerViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.Locale


private const val LOCATION_PERMISSION_REQUEST_CODE =34
class MainActivity : ComponentActivity() {


    private val viewModel: CityExplorerViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    var cityName1: String = ""
    private val _liveDataCityName = MutableLiveData<String>()
    val liveDataCityName: LiveData<String> = _liveDataCityName
    private val _liveDataTemp = MutableLiveData<String>()
    val liveDataTemp: LiveData<String> = _liveDataTemp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "welcome_screen"
                ) {
                    composable("welcome_screen") {
                        WelcomeScreenView(navController)
                    }
                    composable("second_screen") {
                        SecondScreen(navController)
                    }
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = com.google.android.gms.location.LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    currentLocation = locationResult.lastLocation

                    var lat = currentLocation!!.latitude
                    var long = currentLocation!!.longitude
                    val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                    val addresses: MutableList<Address>? = geocoder.getFromLocation(lat, long, 1)
                    val test = addresses?.get(0)?.getAddressLine(0).toString()
                    _liveDataCityName.postValue(test)
                    fetchCity(viewModel, test)
                }
            },
            Looper.myLooper()!!
        )
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
}
