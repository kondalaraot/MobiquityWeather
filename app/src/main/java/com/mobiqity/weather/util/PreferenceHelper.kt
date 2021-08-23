package com.mobiqity.weather.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobiqity.weather.data.Location
import org.json.JSONArray
import org.json.JSONException


class PreferenceHelper constructor(){

    companion object {
        private val sharePref = PreferenceHelper()
        private lateinit var sharedPreferences: SharedPreferences

        private val PLACE_OBJ = "place_obj"

        fun getInstance(context: Context): PreferenceHelper {
            if (!::sharedPreferences.isInitialized) {
                synchronized(PreferenceHelper::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
                    }
                }
            }
            return sharePref
        }
    }

    val placeObj: String
        get() = sharedPreferences.getString(PLACE_OBJ, "")!!

    fun savePlaceObj(place: Location) {
        lateinit var locations:ArrayList<Location>
        val gson = Gson()
        val jsonSaved = sharedPreferences.getString(PLACE_OBJ, "")

        var jsonArrayLocation = JSONArray()

        try {
            if (jsonSaved!!.isNotEmpty()) {
                // below line is to get the type of our array list.
                val type = object : TypeToken<ArrayList<Location?>?>() {}.type
                // in below line we are getting data from gson
                // and saving it to our array list
                locations = gson.fromJson(jsonSaved, type)
                locations.add(place)
            }else{
                locations = ArrayList<Location>()
                locations.add(place)
            }
//            jsonArrayLocation.put(Gson().toJson(locations))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val editor = sharedPreferences.edit()
        editor.putString(PLACE_OBJ, Gson().toJson(locations))
        editor.commit()

    }

    fun saveLocations(locations:ArrayList<Location>){
        val editor = sharedPreferences.edit()
        editor.putString(PLACE_OBJ, Gson().toJson(locations))
        editor.commit()

    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

}
