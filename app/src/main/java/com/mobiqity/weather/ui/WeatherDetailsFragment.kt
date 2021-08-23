package com.mobiqity.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mobiqity.weather.api.APIResult
import com.mobiqity.weather.data.Location
import com.mobiqity.weather.databinding.WeatherDetailsFragmentBinding
import com.mobiqity.weather.util.DialogUtils

class WeatherDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherDetailsFragment()
    }

    private lateinit var location: Location
    private lateinit var binding: WeatherDetailsFragmentBinding
    private val viewModel: WeatherDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeatherDetailsFragmentBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        location = WeatherDetailsFragmentArgs.fromBundle(requireArguments()).location!!

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getWeatherInfo(location)
        registerObservers()
    }

    private fun registerObservers(){
        viewModel.weatherRespModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is APIResult.Loading -> binding.progressBar.visibility = View.VISIBLE
                is APIResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.weather = it.data

                }
                is APIResult.Failure -> binding.progressBar.visibility = View.GONE
                else -> {
                    binding.progressBar.visibility = View.GONE
                    DialogUtils.showErrorToast(requireContext())
                }
            }
        })
    }

}