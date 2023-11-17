package com.example.myapplication.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationService {
    /**
     * Manages all location related tasks for the app.
     */

//A callback for receiving notifications from the FusedLocationProviderClient.
    lateinit var locationCallback: LocationCallback

    //The main entry point for interacting with the Fused Location Provider
    lateinit var locationProvider: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    @Composable
    fun getUserLocation(context: Context): LatandLong {

        // The Fused Location Provider provides access to location APIs.
        locationProvider = LocationServices.getFusedLocationProviderClient(context)

        var currentUserLocation:LatandLong


            locationCallback = object : LocationCallback() {
                //1
                override fun onLocationResult(result: LocationResult) {
                    /**
                     * Option 1
                     * This option returns the locations computed, ordered from oldest to newest.
                     * */
                    for (location in result.locations) {
                        // Update data class with location data
                        currentUserLocation = LatandLong(location.latitude, location.longitude)

                    }
                }
            }
            //2
            if (hasPermissions(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                locationUpdate()
            } else {
                askPermissions(
                    context, REQUEST_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            //3
            onDispose {
                stopLocationUpdate()
            }

        //4
        return currentUserLocation
    }
}
    //data class to store the user Latitude and longitude
    data class LatandLong(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    )