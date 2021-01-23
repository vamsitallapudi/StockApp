package com.coderefer.stockapp.data.api

import com.coderefer.stockapp.data.entity.Stock
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {

    @GET("weather")
    fun getWeatherAsync(
        @Query("q") city: String,
        @Query("appid") appId: String,
        @Query("units") units: String
    ) : Deferred<Response<Stock>>

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }
}