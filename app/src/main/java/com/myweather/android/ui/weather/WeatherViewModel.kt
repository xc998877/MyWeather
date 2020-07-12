package com.myweather.android.ui.weather


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.myweather.android.logic.Repository
import com.myweather.android.logic.model.Location

class WeatherViewModel:ViewModel() {
    private val locationLiveData=MutableLiveData<Location>()
    var locationLng=""
    var locationLat=""
    var placeName=""
    val weatherLiveData=Transformations.switchMap(locationLiveData){location->
        Repository.refreshWeather(locationLng,locationLat)
    }
    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value= Location(lng,lat)
    }
}