package com.mobiqity.weather.data

import java.io.Serializable

data class Location(
    val locationName:String,
    val lat:Double,
    val long:Double
):Serializable