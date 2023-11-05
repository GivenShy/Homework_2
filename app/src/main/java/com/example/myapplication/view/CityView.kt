package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.City

@Composable
fun CityView(modifier:Modifier=Modifier,city: City){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(text = city.name,
            fontSize = 20.sp)
        Spacer(modifier = Modifier.size(15.dp))
        Image(
            painter = painterResource(id = city.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(height = 200.dp, width = 300.dp)
        )
        Text(text = city.description)
        Spacer(modifier = Modifier.size(30.dp))
    }
}