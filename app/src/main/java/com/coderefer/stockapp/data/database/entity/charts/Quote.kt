package com.coderefer.stockapp.data.database.entity.charts

data class Quote(
    val volume: List<String>,
    val high: List<String>,
    val low: List<String>,
    val close: List<String>,
    val open: List<String>
)