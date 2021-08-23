package com.mobiqity.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiqity.weather.api.APIResult
import com.mobiqity.weather.data.AppRepo
import com.mobiqity.weather.data.Location
import com.mobiqity.weather.data.WeatherRespModel
import kotlinx.coroutines.launch

class WeatherDetailsViewModel : ViewModel() {

    val appRepo: AppRepo = AppRepo()

    private val _weatherRespModel: MutableLiveData<APIResult<WeatherRespModel>> =
        MutableLiveData()
    val weatherRespModel: LiveData<APIResult<WeatherRespModel>> = _weatherRespModel

    fun getWeatherInfo(location: Location) {
        viewModelScope.launch {
            try {
                _weatherRespModel.value = APIResult.Loading(true)
                val resp = appRepo.getWeatherByGeoLocation(location)
                _weatherRespModel.value = when (resp.cod) {
                    200 -> APIResult.Success(resp)
                    else -> APIResult.Failure(resp)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _weatherRespModel.value = APIResult.Error(
                    exception = e,
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            }
        }
    }
}