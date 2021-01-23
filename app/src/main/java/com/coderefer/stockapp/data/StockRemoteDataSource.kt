package com.coderefer.stockapp.data

import android.content.Context
import com.coderefer.stockapp.BuildConfig
import com.coderefer.stockapp.util.*
import com.coderefer.stockapp.data.api.StockService
import com.coderefer.stockapp.data.api.StockService.Companion.BASE_URL
import com.coderefer.stockapp.data.entity.Stock
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

//TODO: to add DI
class StockRemoteDataSource(val context: Context) : StockDataSource {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor())
            .cache(Cache(getCacheFile(context), 10 * 1024 * 1024))
            .build()

    }

    private fun getCacheFile(context:Context) : File {
        val cacheFile = File(context.cacheDir, "okhttp_cache")
        cacheFile.mkdirs()
        return cacheFile
    }

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        val debugLevel = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return HttpLoggingInterceptor().apply {
            level = debugLevel
        }
    }

    val service by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(StockService::class.java)
    }

    suspend fun fetchWeather() : Flow<Result<Stock>> {
        return safeApiCall(call = {
            fetchStock
        }, NETWORK_ERROR_MSG)
    }

    private val fetchStock:Flow<Result<Stock>> = flow {
        while(true) {
            try {
               val response = service.getWeatherAsync("Hyderabad", OPEN_WEATHER_APPID, WEATHER_UNIT).await()
                if (response.isSuccessful) {
                    val weather = response.body()
                    if (weather!= null) {
                        emit(Result.Success(weather))
                        delay(FETCH_DELAY_MS)
                    } else {
                        emit(Result.Error(IOException(NETWORK_ERROR_MSG)))
                    }
                }
            } catch (e: Exception) {


                emit(Result.Error(IOException(e.toString())))
                delay(FETCH_DELAY_MS)
            }
        }
    }

}