package com.coderefer.stockapp.data.api

import com.coderefer.stockapp.data.entity.Stock
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

    companion object {
        const val BASE_URL = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/"
    }
}