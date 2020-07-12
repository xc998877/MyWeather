package com.myweather.android.logic.network

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object MyWeatherNetwork {
    private val placeService=ServiceCreator.create(PlaceService::class.java)
    private val weatherService=ServiceCreator.create(WeatherService::class.java)

    suspend fun searchPlaces(query:String)= placeService.searchPlaces(query).await()
    suspend fun getDailyWeather(lng:String,lat:String)= weatherService.getDailyWeather(lng,lat).await()
    suspend fun getRealtimeWeather(lng:String,lat: String)= weatherService.getRealtimeWeather(lng,lat).await()
    private suspend fun <T> Call<T>.await():T{
        return suspendCancellableCoroutine {continuation->
            enqueue(object:Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if(body!=null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("没有数据"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}