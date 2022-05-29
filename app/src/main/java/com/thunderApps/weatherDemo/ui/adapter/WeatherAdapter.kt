package com.thunderApps.weatherDemo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.thunderApps.weatherDemo.R
import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.databinding.WeatherCardBinding
import com.thunderApps.weatherDemo.ui.util.UiUtil.loadFromUrl
import com.thunderApps.weatherDemo.ui.util.UiUtil.toGone
import com.thunderApps.weatherDemo.ui.util.UiUtil.toVisible
import com.thunderApps.weatherDemo.util.DateTimeUtil.toFormattedString_MMM_dd_h_mm
import java.time.LocalDateTime
import java.time.OffsetDateTime

class WeatherAdapter(
    val list: List<WeatherApiResponse>,
    val listener: (WeatherApiResponse, Int) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private val TAG = "WeatherAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WeatherCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val itemBinding: WeatherCardBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: WeatherApiResponse, position: Int) {
            val context = itemBinding.root.context
            try {
                itemBinding.apply {
                    textDescription.text = context.getString(R.string.weather_description, item.weather[0].description.capitalize(), item.name)
                    image.loadFromUrl(item.weather[0].getIconUrl())
                    textTemp.text = context.getString(R.string.temp_data, item.main.temp)
                    item.wind.speed?.let { textWindSpeed.text = context.getString(R.string.wind_speed, it) }
                    item.main.humidity?.let { textHumidity.text = context.getString(R.string.humidity, it) }
                    root.setOnClickListener {
                        if (itemBinding.layoutMore.isVisible) {
                            itemBinding.layoutMore.toGone()
                        } else {
                            itemBinding.layoutMore.toVisible()
                        }
                    }
                    textDate.text = context.getString(R.string.date_data, LocalDateTime.ofEpochSecond(item.dt, 0, OffsetDateTime.now().offset)
                            .toFormattedString_MMM_dd_h_mm())
                }
            } catch (e: Exception) {
                Log.e(TAG, "bind: ", e)
            }
        }
    }
}