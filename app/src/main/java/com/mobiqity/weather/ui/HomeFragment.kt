package com.mobiqity.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobiqity.weather.R
import com.mobiqity.weather.adapters.LocationsAdapter
import com.mobiqity.weather.data.Location
import com.mobiqity.weather.databinding.HomeFragmentBinding
import com.mobiqity.weather.ui.HomeFragment.Callback

class HomeFragment : Fragment(), LocationsAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }
    var locations: ArrayList<Location> = ArrayList<Location>()
    private val adapter:LocationsAdapter = LocationsAdapter(this)
    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater,container,false).apply {
            viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            rvLocations.adapter = adapter
            callback = Callback {
                findNavController().navigate(R.id.action_home_fragment_to_add_location_fragment)
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        locations = viewModel.getLocations()
        if (locations.isEmpty()){
            binding.tvNoLocations.visibility = View.VISIBLE
        }else{
            binding.tvNoLocations.visibility = View.GONE
        }
        adapter.submitData(locations = locations)
    }

    override fun onResume() {
        super.onResume()
        if (locations.isNotEmpty()){
            adapter.submitData(locations = locations)
        }
    }

    fun interface Callback {
        fun addLocation()
    }

    override fun onDeleteLocation(location:Location) {
        locations.remove(location)
        adapter.notifyDataSetChanged()
        viewModel.saveLocations(locations)
    }

    override fun onItemClick(location: Location) {
        val action = HomeFragmentDirections.actionHomeFragmentToAddWeatherDetails(location)
        findNavController().navigate(action)
//        findNavController().navigate(R.id.action_home_fragment_to_add_weather_details)
    }

}