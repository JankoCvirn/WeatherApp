package com.cvirn.weathercvirn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.databinding.CityForecastFragmentBinding
import com.cvirn.weathercvirn.model.CityForecastData
import com.cvirn.weathercvirn.view.adapter.DailyItemAdapter
import com.cvirn.weathercvirn.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CityForecastFragment : Fragment() {

    private lateinit var binding: CityForecastFragmentBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityForecastFragmentBinding.inflate(inflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.cityDailyWeatherObservable.observe(viewLifecycleOwner) { event ->
            event.cityForecast?.let {
                binding.txtLocationName.text =
                    getString(R.string.daily_forecast_placeholder, it.name)
                populateData(it.daily)
            }
        }
    }

    private fun populateData(daily: List<CityForecastData.CityForecast.Daily>?) {
        val adapter = DailyItemAdapter()
        daily?.let {
            adapter.setData(it)
            binding.recycler.adapter = adapter
        }
    }
}
