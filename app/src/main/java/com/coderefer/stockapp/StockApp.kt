package com.coderefer.stockapp

import android.app.Application
import android.content.Context
import com.coderefer.stockapp.data.CoroutineDispatchProvider
import com.coderefer.stockapp.data.StockLocalDataSource
import com.coderefer.stockapp.data.StockLocalDataSource.Companion.STOCK_PREFS_SET
import com.coderefer.stockapp.data.StockRemoteDataSource
import com.coderefer.stockapp.data.StockRepo
import com.coderefer.stockapp.util.STOCK_PREFS

class StockApp : Application() {
    private val localDataSource by lazy { StockLocalDataSource(getSharedPreferences(STOCK_PREFS, Context.MODE_PRIVATE )) }
    private val remoteDataSource by lazy { StockRemoteDataSource(applicationContext) }

    private val isDataFromMock = false

    private val set = mutableSetOf("BSE","AMZN","NFLX","UBER","BARC")

    val stockRepo by lazy {
//        TODO: provide dispatch provider from DI
        StockRepo(set.toList(),localDataSource,remoteDataSource, CoroutineDispatchProvider())
    }
}