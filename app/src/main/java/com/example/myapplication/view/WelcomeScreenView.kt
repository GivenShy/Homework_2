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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.CityExplorerViewModel


@Composable
fun WelcomeScreenView(navController: NavController, modifier:Modifier = Modifier, viewModel: CityExplorerViewModel = viewModel()){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to our City Explorer App!",
            fontSize = 16.sp)
        Spacer(modifier = modifier.size(50.dp))
        //Text(text = )
        Button(
            onClick = {
                navController.navigate("second_screen")
                      },
            ) {
                Text(text = "Explore Cities")
            }
        }

}