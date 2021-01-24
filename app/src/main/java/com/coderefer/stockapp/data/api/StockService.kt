package com.coderefer.stockapp.data.api

import com.coderefer.stockapp.data.database.entity.Stock
import com.coderefer.stockapp.data.database.entity.charts.ChartResp
import com.coderefer.stockapp.util.CHART_INTERVAL
import com.coderefer.stockapp.util.DEFAULT_CHART_RANGE
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {

    @GET("market/v2/get-quotes")
    fun getQuotesAsync(
        @Query("region") region: String,
        @Query("symbols") symbols: String
    ) : Deferred<Response<Stock>>

    @GET("market/get-charts")
    fun getChartsAsync(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String = CHART_INTERVAL,
        @Query("range") range: String = DEFAULT_CHART_RANGE
    ) : Deferred<Response<ChartResp>>

    companion object {
        const val BASE_URL = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/"
    }
}