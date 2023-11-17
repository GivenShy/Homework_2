package com.example.myapplication

import android.location.Location
import android.os.Bundle
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
import com.example.myapplication.viewmodel.CityExplorerViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


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


    override fun onStart() {
        super.onStart()


                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )


    }
}
