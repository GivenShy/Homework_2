package com.example.myapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.LocationViewModel


@Composable
fun WelcomeScreenView(navController: NavController, modifier:Modifier = Modifier, viewModel: LocationViewModel = viewModel()){
    var response = viewModel.loadTemp("a8e25257bdb84da9a91184359231411", context = LocalContext.current)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to our City Explorer App!",
            fontSize = 16.sp)
        Spacer(modifier = modifier.size(50.dp))
        Text(text = "Temperature:" + response?.temp?.degreesC)
        Button(
            onClick = {
                navController.navigate("second_screen")
                      },
            ) {
                Text(text = "Explore Cities")
            }
        }

}