package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.view.SecondScreen
import com.example.myapplication.view.SettingsPage
import com.example.myapplication.view.WelcomeScreenView
import com.google.android.gms.location.LocationServices


private const val LOCATION_PERMISSION_REQUEST_CODE =34
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)

        setContent {

            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "welcome_screen"
                ) {
                    composable("welcome_screen") {
                        WelcomeScreenView(this@MainActivity,fusedLocationClient,navController)
                    }
                    composable("second_screen") {
                        SecondScreen(navController,this@MainActivity)
                    }
                    composable("settings_screen") {
                        SettingsPage(navController,this@MainActivity)
                    }
                }
            }
        }
    }
}
