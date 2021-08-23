package com.mobiqity.weather.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobiqity.weather.data.Location
import com.mobiqity.weather.util.PreferenceHelper
import java.lang.reflect.Type


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    fun getLocations():ArrayList<Location>{
        try {
            val locationsJsonArray = PreferenceHelper.getInstance(getApplication()).placeObj
            return if (locationsJsonArray.isEmpty()){
                ArrayList<Location>()
            }else{
                // below line is to get the type of our array list.
                val type: Type = object : TypeToken<List<Location?>?>() {}.type
                Gson().fromJson(locationsJsonArray, type)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
//            Gson().fromJson(locationsJsonArray , Array<Location>::class.java).toList() as ArrayList<Location>
        return ArrayList<Location>()
    }

    fun saveLocations(locations:ArrayList<Location>){
        PreferenceHelper.getInstance(getApplication()).saveLocations(locations)
    }

}