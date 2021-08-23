package com.mobiqity.weather.data

import com.mobiqity.weather.api.WeatherAPIService
import com.mobiqity.weather.util.Constants

class AppRepo {

    private val service  = WeatherAPIService.create()

    suspend fun getWeatherByLocation(location: Location){
        service.getByLatLong(location.lat,location.long, Constants.API_KEY)
    }

    suspend fun getWeatherByGeoLocation(location: Location):WeatherRespModel{
        return service.getByLatLong(location.lat,location.long, Constants.API_KEY)
    }
}