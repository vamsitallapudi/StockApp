package com.coderefer.stockapp.util

const val NETWORK_TIMEOUT = 15L
const val YAHOO_API_ID_KEY = "x-rapidapi-key"
const val YAHOO_API_HOST_KEY = "x-rapidapi-host"
const val YAHOO_API_HOST = "apidojo-yahoo-finance-v1.p.rapidapi.com"

const val DEFAULT_STOCKS = "BSE,NSE,GOLD,AAPL,GOOGL,TSLA,MSFT,AMZN,NFLX,UBER,BARC"
const val DEFAULT_CHART_STOCK = "AAPL"

const val FETCH_DELAY_MS = 1000 * 30L

const val SEARCH_DELAY_TIMER = 1000L

const val STOCK_PREFS = "STOCK_PREFS"

const val REGION = "IN"

const val NETWORK_ERROR_MSG = "Unable to fetch data from the Network."

const val CACHE_SIZE = 10 * 1024 * 1024L

val CHART_INTERVAL = "5m"
val CHART_RANGE = "1d"
