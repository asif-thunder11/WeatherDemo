package com.thunderApps.weatherDemo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.databinding.WeatherCardBinding
import com.thunderApps.weatherDemo.util.DateTimeUtil.toFormattedString_MMM_dd
import com.thunderApps.weatherDemo.util.Util.loadFromUrl
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

class WeatherAdapter(val list: List<WeatherApiResponse>, val listener: (WeatherApiResponse, Int) -> Unit) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private val TAG = "WeatherAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(WeatherCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val itemBinding: WeatherCardBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: WeatherApiResponse, position: Int) {

            try {
                itemBinding.apply {
                    textDescription.text = item.weather[0].description
                    image.loadFromUrl(item.weather[0].getIconUrl())
                    root.setOnClickListener { listener(item, position) }
                    textDate.text = LocalDateTime.ofEpochSecond(item.dt, 0, OffsetDateTime.now().offset).toFormattedString_MMM_dd()
                }
            } catch (e: Exception) {
                Log.e(TAG, "bind: ", e)
            }
        }
    }
}