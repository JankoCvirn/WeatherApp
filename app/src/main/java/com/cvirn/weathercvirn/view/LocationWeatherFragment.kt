package com.cvirn.weathercvirn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.work.*
import coil.load
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.databinding.LocationWeatherFragmentBinding
import com.cvirn.weathercvirn.utils.buildIconUrl
import com.cvirn.weathercvirn.viewmodel.MainViewModel
import com.cvirn.weathercvirn.worker.ForecastWorker
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit

class LocationWeatherFragment : Fragment() {

    private lateinit var binding: LocationWeatherFragmentBinding
    private val viewModel by sharedViewModel<MainViewModel>()
    private val workManager by lazy {
        WorkManager.getInstance(requireContext())
    }

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
        setupListener()
        setupUI()
    }

    private fun setupUI() {
        binding.switchAlert.isChecked = isWorkerRunning()
    }

    private fun setupListener() {
        binding.switchAlert.setOnCheckedChangeListener { _, b ->
            if (b) {
                startWorker()
            } else {
                stopWorker()
            }
        }
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
            binding.switchAlert.isVisible = true
        }
        viewModel.cachedForecast.observe(viewLifecycleOwner) {
            it?.let {
                if (!it.locationForecast?.name.isNullOrBlank()) {
                    binding.progressBar.isVisible = false
                    binding.switchAlert.isVisible = true
                    binding.txtLocationName.text = getString(
                        R.string.current_location_placeholder,
                        it.locationForecast?.name
                    )
                    binding.imgIcon.load(it.locationForecast?.weather?.icon?.buildIconUrl())
                    binding.txtLocationWeather.text = getString(
                        R.string.current_location_weather_placeholder,
                        it.locationForecast?.weather?.main,
                        it.locationForecast?.weather?.description
                    )
                    binding.txtLocationDetails.text = getString(
                        R.string.current_location_details_placeholder,
                        it?.locationForecast?.main?.temp.toString(),
                        it?.locationForecast?.main?.tempMin.toString(),
                        it?.locationForecast?.main?.tempMax.toString(),
                        it?.locationForecast?.main?.pressure.toString(),
                        it?.locationForecast?.main?.humidity.toString(),
                        it?.locationForecast?.wind?.speed.toString()
                    )
                }
            }
        }
    }

    private fun startWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val worker = PeriodicWorkRequestBuilder<ForecastWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInitialDelay(30, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, worker)
    }

    private fun stopWorker() {
        workManager.cancelAllWork()
    }

    private fun isWorkerRunning(): Boolean {
        return workManager.getWorkInfosForUniqueWork(WORKER_TAG).get().size > 0
    }

    companion object {
        private const val WORKER_TAG = "weather_worker"
    }
}
