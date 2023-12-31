package com.example.myapplication.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.CityExplorerViewModel


@Composable
fun SecondScreen(navController: NavController, viewModel: CityExplorerViewModel = viewModel()) {
    Column {
        Text(text = "Cities",
            fontSize = 21.sp,
            modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.size(5.dp))
        Column {
            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false),
            ) {
                for (city in viewModel.getCities()){
                    CityView(city = city)
                }
            }
            Button(
                onClick = { navController.navigate("welcomeView") },
                modifier = Modifier
                    .paddingFromBaseline(0.dp, 30.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Back")
            }
        }
    }
}