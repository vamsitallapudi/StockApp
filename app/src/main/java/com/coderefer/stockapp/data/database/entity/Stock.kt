package com.coderefer.stockapp.data.database.entity

import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("quoteResponse")
    val stockQuote: StockQuote
    )
