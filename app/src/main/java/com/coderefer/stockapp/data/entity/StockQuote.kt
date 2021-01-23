package com.coderefer.stockapp.data.entity

import com.google.gson.annotations.SerializedName

class StockQuote(
    @SerializedName("result")
    val stockResults : List<StockResult>)
