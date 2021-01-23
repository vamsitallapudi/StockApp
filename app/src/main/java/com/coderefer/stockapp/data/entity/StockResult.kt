package com.coderefer.stockapp.data.entity

import com.google.gson.annotations.SerializedName

data class StockResult(
    @SerializedName("regularMarketPrice")
    val regularMarketPrice: String,
    @SerializedName("regularMarketPreviousClose")
    val regularMarketPreviousClose: String
)