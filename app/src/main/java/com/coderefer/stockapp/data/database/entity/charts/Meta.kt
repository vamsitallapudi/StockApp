package com.coderefer.stockapp.data.database.entity.charts

data class Meta(
    val exchangeTimezoneName: String?,

    val symbol: String?,

    val instrumentType: String?,

    val firstTradeDate: String?,

    val timezone: String?,

    val scale: String?,

    val range: String?,

    val regularMarketTime: String?,

    val dataGranularity: String?,

    val validRanges: List<String>,

    val regularMarketPrice: String?,

    val tradingPeriods: List<List<TradingPeriods>>,

    val previousClose: String?,

    val gmtoffset: String?,

    val chartPreviousClose: String?,

    val priceHint: String?,

    val currency: String?,

    val exchangeName: String?,

    val currentTradingPeriod: CurrentTradingPeriod?
)
