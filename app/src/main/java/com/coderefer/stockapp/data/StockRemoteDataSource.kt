package com.coderefer.stockapp.data

import android.content.Context
import android.util.Log
import com.coderefer.stockapp.BuildConfig
import com.coderefer.stockapp.util.*
import com.coderefer.stockapp.data.api.StockService
import com.coderefer.stockapp.data.api.StockService.Companion.BASE_URL
import com.coderefer.stockapp.data.database.entity.Stock
import com.coderefer.stockapp.data.database.entity.charts.ChartResp
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

//TODO: to add DI
class StockRemoteDataSource(private val context: Context) : StockDataSource {
    private var stocks: String = DEFAULT_STOCKS

    private var chartStock: String = DEFAULT_CHART_STOCK
    private var chartRange: String = DEFAULT_CHART_RANGE

    private val okHttpClient by lazy {
        OkHttpClient.Builder().apply {
            addInterceptor(networkInterceptor)
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor())
            cache(Cache(getCacheFile(context), CACHE_SIZE))
        }.build()

    }

    private val networkInterceptor = Interceptor { chain ->
        val builder = chain.request().newBuilder()
        builder.header(YAHOO_API_ID_KEY, YAHOO_API_ID)
        builder.header(YAHOO_API_HOST_KEY, YAHOO_API_HOST)
        builder.header("useQueryString", "true")
        return@Interceptor chain.proceed(builder.build())
    }

    private fun getCacheFile(context: Context): File {
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

    suspend fun fetchStocks(stockName: String?): Flow<Result<Stock>> {
        stockName?.let { stocks = it }
        return safeApiCall(call = {
            fetchStock
        }, NETWORK_ERROR_MSG)
    }

    private val fetchStock: Flow<Result<Stock>> = flow {
        while (true) {
            try {
                val response = service.getQuotesAsync(REGION, stocks).await()
                if (response.isSuccessful) {
                    val stock = response.body()
                    if (stock != null) {
                        emit(Result.Success(stock))
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

    suspend fun fetchCharts(stockName: String?, range: String?): Flow<Result<ChartResp>> {
        stockName?.let { chartStock = it }
        range?.let { chartRange = it }
        return safeApiCall(call = {
            fetchChart
        }, NETWORK_ERROR_MSG)
    }

    private val fetchChart: Flow<Result<ChartResp>> = flow {
        try {
            var chartInterval = CHART_INTERVAL
            if(chartRange =="3M") {
                chartInterval = "15m"
            } else if (chartRange == "6m") {
                chartInterval = "15m"
            }
            val response = service.getChartsAsync(chartStock, chartInterval, chartRange).await()
            if (response.isSuccessful) {
                val chart = response.body()
                if (chart != null) {
                    emit(Result.Success(chart))
                } else {
                    emit(Result.Error(IOException(NETWORK_ERROR_MSG)))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(IOException(e.toString())))
        }
    }

}