package com.cvirn.weathercvirn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.databinding.LocationWeatherFragmentBinding
import com.cvirn.weathercvirn.utils.buildIconUrl
import com.cvirn.weathercvirn.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LocationWeatherFragment : Fragment() {

    private lateinit var binding: LocationWeatherFragmentBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationWeatherFragmentBinding.inflate(inflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.progressObservable.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it ?: false
        }
        viewModel.forecastObservable.observe(viewLifecycleOwner) {
            binding.txtLocationName.text = getString(
                R.string.current_location_placeholder,
                it.peekContent().locationForecast?.name
            )
            binding.imgIcon.load(it.peekContent().locationForecast?.weather?.icon?.buildIconUrl())
            binding.txtLocationWeather.text = getString(
                R.string.current_location_weather_placeholder,
                it.peekContent().locationForecast?.weather?.main,
                it.peekContent().locationForecast?.weather?.description
            )
            binding.txtLocationDetails.text = getString(
                R.string.current_location_details_placeholder,
                it.peekContent().locationForecast?.main?.temp.toString(),
                it.peekContent().locationForecast?.main?.tempMin.toString(),
                it.peekContent().locationForecast?.main?.tempMax.toString(),
                it.peekContent().locationForecast?.main?.pressure.toString(),
                it.peekContent().locationForecast?.main?.humidity.toString(),
                it.peekContent().locationForecast?.wind?.speed.toString()
            )
        }
    }
}
