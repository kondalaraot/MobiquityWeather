package com.mobiqity.weather.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mobiqity.weather.data.Location
import com.mobiqity.weather.util.PreferenceHelper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class AddLocationViewModel : ViewModel() {


    fun saveLocation(location: Location,context:Context){
        PreferenceHelper.getInstance(context).savePlaceObj(location)
    }
}