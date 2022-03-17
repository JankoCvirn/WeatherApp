package com.cvirn.weathercvirn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cvirn.weathercvirn.databinding.CityForecastFragmentBinding
import com.cvirn.weathercvirn.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CityForecastFragment : Fragment() {

    private lateinit var binding: CityForecastFragmentBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CityForecastFragmentBinding.inflate(inflater, null, false)
        return binding.root
    }
}