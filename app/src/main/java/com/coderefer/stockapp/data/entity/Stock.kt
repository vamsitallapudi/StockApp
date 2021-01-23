package com.coderefer.stockapp.data.entity

import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("weather")
    val details: List<StockDetails>,
    @SerializedName("main")
    val temp: Temperature
)
