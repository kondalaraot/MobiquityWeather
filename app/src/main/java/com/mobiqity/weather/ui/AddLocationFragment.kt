package com.mobiqity.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.mobiqity.weather.R
import com.mobiqity.weather.databinding.AddLocationFragmentBinding
import java.io.IOException
import java.util.*


class AddLocationFragment : Fragment(), OnMapReadyCallback ,
    ActivityCompat.OnRequestPermissionsResultCallback{

    companion object {
        const val TAG = "AddLocationFragment"
        fun newInstance() = AddLocationFragment()

    }

    private var map: GoogleMap? = null

    var currentMarker: Marker? = null

//    private lateinit var mMap: GoogleMap
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 101
    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    private lateinit var binding: AddLocationFragmentBinding
    private val viewModel: AddLocationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddLocationFragmentBinding.inflate(inflater, container, false).apply {
            mapView.onCreate(savedInstanceState);
            mapView.onResume()
            try {
                MapsInitializer.initialize(requireActivity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            btnAdd.setOnClickListener(View.OnClickListener {
                viewModel.saveLocation(com.mobiqity.weather.data.Location(binding.tvLocName.text.toString(),
                    currentMarker?.position!!.latitude,currentMarker?.position!!.longitude),requireContext())
                requireActivity().onBackPressed()
            })
//            mapView.getMapAsync(this@AddLocationFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        Places.initialize(requireContext(), getString(R.string.google_maps_key))
//        placesClient = Places.createClient(requireContext())

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        binding.mapView.getMapAsync(this)
        fetchLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG,"onMapReady")
        this.map = googleMap
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
        map?.uiSettings?.isZoomControlsEnabled = true
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        moveMarket(latLng)

        googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                Log.d("====", "latitude : " + marker.position.latitude)

                if (currentMarker != null) {
                    currentMarker?.remove()
                }
                val newlatLng = LatLng(marker.position.latitude, marker.position.longitude)
                moveMarket(newlatLng)
            }

            override fun onMarkerDrag(marker: Marker) {}
        })

    }

    private fun moveMarket(latLng: LatLng) {

        val markerOptions = MarkerOptions().position(latLng).title("I am here")
            .snippet(getTheAddress(latLng!!.latitude, latLng!!.longitude)).draggable(true)
        map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
        currentMarker = map?.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    private fun getTheAddress(latitude: Double, longitude: Double): String? {
        var retVal = ""
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            retVal = addresses[0].getAddressLine(0)
            binding.tvLocName.text = addresses[0].locality
            binding.btnAdd.isEnabled = addresses[0].locality!=null

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return retVal
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation

        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(requireContext(), currentLocation!!.latitude.toString() + "" + currentLocation!!.longitude,
                    Toast.LENGTH_SHORT).show()
//                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
                binding.mapView.getMapAsync(this)
            }
        }
    }

}