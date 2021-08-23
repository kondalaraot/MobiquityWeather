package com.mobiqity.weather.api

import com.mobiqity.weather.data.WeatherRespModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {


    companion object {
//        private const val BASE_URL = "https://api.unsplash.com/"
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun create(): WeatherAPIService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherAPIService::class.java)
        }
    }

    @GET("weather")
    suspend fun getByCityName(@Query("q") cityName: String,
                                   @Query("appid") apiKey: String,
                                   ):WeatherRespModel
    @GET("weather")
    suspend fun getByLatLong(@Query("lat") lat: Double,
                                   @Query("lon") lon: Double,
                                   @Query("appid") apiKey: String,
                                   ):WeatherRespModel
}
