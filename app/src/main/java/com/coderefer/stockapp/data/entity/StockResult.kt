package com.coderefer.stockapp.data.entity

import com.google.gson.annotations.SerializedName

data class StockResult(
    @SerializedName("regularMarketPrice")
    val regularMarketPrice: Double,
    @SerializedName("regularMarketPreviousClose")
    val regularMarketPreviousClose: Double,
    @SerializedName("symbol")
    val symbol: String
)