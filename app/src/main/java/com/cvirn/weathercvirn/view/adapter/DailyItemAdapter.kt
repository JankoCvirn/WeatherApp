package com.cvirn.weathercvirn.view.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.databinding.CityWeatherItemBinding
import com.cvirn.weathercvirn.model.CityForecastData
import com.cvirn.weathercvirn.utils.buildIconUrl
import com.cvirn.weathercvirn.utils.getDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DailyItemAdapter : RecyclerView.Adapter<DailyItemAdapter.ForecastViewHolder>(),
    KoinComponent {

    private val items = arrayListOf<CityForecastData.CityForecast.Daily>()
    private val context: Context by inject()

    fun setData(list: List<CityForecastData.CityForecast.Daily>) {
        val diffCallback = DiffCallback(items, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityWeatherItemBinding.inflate(inflater, parent, false)
        return ForecastViewHolder(binding, context)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(item = items[position])
    }

    class ForecastViewHolder(private val binding: CityWeatherItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CityForecastData.CityForecast.Daily) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.txtDate.text = item.dt?.getDate()
            }
            binding.imgIcon.load(item.icon?.buildIconUrl())
            binding.txtLocationWeather.text = context.getString(
                R.string.current_location_weather_placeholder,
                item.mainWeather,
                item.descriptionWeather
            )
            binding.txtLocationDetails.text = context.getString(
                R.string.current_location_details_placeholder,
                item.dayTemp.toString(),
                item.minTemp.toString(),
                item.maxTemp.toString(),
                item.pressure.toString(),
                item.humidity.toString(),
                item.windSpeed.toString()
            )
        }
    }

    class DiffCallback(
        private val old: List<CityForecastData.CityForecast.Daily>,
        private val new: List<CityForecastData.CityForecast.Daily>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == new[newItemPosition]

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = old[oldItemPosition]
            val new = new[newItemPosition]
            return old == new
        }
    }
}
