package com.coderefer.stockapp.data.database.entity

import com.google.gson.annotations.SerializedName

class StockQuote(
    @SerializedName("result")
    val stockResults : List<StockResult>)
