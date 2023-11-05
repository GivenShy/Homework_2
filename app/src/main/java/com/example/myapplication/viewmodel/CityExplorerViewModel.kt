package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.R

data class City(
    val name: String,
    val description: String,
    val imageUrl: Int
)
class CityExplorerViewModel : ViewModel() {
    private val cities = listOf(
        City("Yerevan","Yerevan, is the capital and largest city of Armenia and one of the world's oldest continuously inhabited cities. Situated along the Hrazdan River, Yerevan is the administrative, cultural, and industrial center of the country, as its primate city.", R.drawable.yerevan),
        City("Washington", "Washington, D.C., formally the District of Columbia and commonly called Washington or D.C., is the capital city and the federal district of the United States. The city is located on the east bank of the Potomac River, which forms its southwestern border with Virginia and borders Maryland to its north and east.",R.drawable.washington ),
        City("Madrid","Madrid is the capital and most populous city of Spain. The city has almost 3.4 million inhabitants and a metropolitan area population of approximately 6.7 million. It is the second-largest city in the European Union (EU), and its monocentric metropolitan area is the second-largest in the EU.",R.drawable.madrid),
        City("Paris","Paris is the capital and most populous city of France. With an official estimated population of 2,102,650 residents as of 1 January 2023 in an area of more than 105 km2 (41 sq mi), Paris is the fifth-most populated city in the European Union and the 30th most densely populated city in the world in 2022.",R.drawable.paris)
    )

    fun getCities(): List<City> {
        return cities
    }


}