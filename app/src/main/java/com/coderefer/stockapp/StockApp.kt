package com.coderefer.stockapp

import android.app.Application
import com.coderefer.stockapp.data.StockLocalDataSource
import com.coderefer.stockapp.data.StockRemoteDataSource
import com.coderefer.stockapp.data.StockRepo

class StockApp : Application() {
    private val localDataSource by lazy { StockLocalDataSource() }
    private val remoteDataSource by lazy { StockRemoteDataSource(applicationContext) }

    private val isDataFromMock = false

    val weatherRepo by lazy {
        StockRepo(localDataSource,remoteDataSource)
    }
}