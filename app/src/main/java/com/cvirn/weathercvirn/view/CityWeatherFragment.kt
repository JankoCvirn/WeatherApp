package com.cvirn.weathercvirn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.cvirn.weathercvirn.BuildConfig
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.databinding.CityWeatherFragmentBinding
import com.cvirn.weathercvirn.model.CityQuery
import com.cvirn.weathercvirn.utils.buildIconUrl
import com.cvirn.weathercvirn.utils.hideKeyboard
import com.cvirn.weathercvirn.utils.isInternetAvailable
import com.cvirn.weathercvirn.utils.toast
import com.cvirn.weathercvirn.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CityWeatherFragment : Fragment() {

    private lateinit var binding: CityWeatherFragmentBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityWeatherFragmentBinding.inflate(inflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etxtSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
        setupListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.cityWeatherObservable.observe(viewLifecycleOwner) {

            binding.txtLocationName.text = getString(
                R.string.search_location_placeholder,
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
        viewModel.progressObservable.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it ?: false
        }

        viewModel.citySearchErrorObservable.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().toast(getString(R.string.city_not_found_error))
            }
        }
    }

    private fun setupListener() {
        binding.etxtSearch.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                requireActivity().hideKeyboard()
                if (!requireContext().isInternetAvailable()) {
                    requireActivity().toast(getString(R.string.enable_internet_connection))
                } else {
                    viewModel.getCityWeather(
                        CityQuery(
                            city = view.editableText.toString(),
                            appId = BuildConfig.API_KEY,
                            units = "metric"
                        )
                    )
                }
                true
            } else false
        }
    }
}
