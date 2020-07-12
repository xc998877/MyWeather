package com.myweather.android.logic

import androidx.lifecycle.liveData
import com.myweather.android.logic.model.Place
import com.myweather.android.logic.model.Weather
import com.myweather.android.logic.network.MyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Dispatcher
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query:String)= fire(Dispatchers.IO) {
            val placeResponse=MyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("响应状态是${placeResponse.status}"))
            }
    }
    fun refreshWeather(lng:String,lat:String)= fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime=async { MyWeatherNetwork.getRealtimeWeather(lng,lat) }
                val deferredDaily=async { MyWeatherNetwork.getDailyWeather(lng,lat) }
                val realtimeResponse=deferredRealtime.await()
                val dailyResponse=deferredDaily.await()
                if(realtimeResponse.status=="ok"&& dailyResponse.status=="ok"){
                    val weather=Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(RuntimeException("实时天气状态码：${realtimeResponse.status} 每日天气状态码：${dailyResponse.status}"))
                }
            }
    }
    private fun <T> fire(context:CoroutineContext,block:suspend()->Result<T>)= liveData<Result<T>>(context) {
        val result=try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}