package com.coderefer.stockapp.data

import kotlinx.coroutines.flow.Flow

class StockRepo(
    private val localDataSource: StockLocalDataSource,
    private val remoteDataSource: StockRemoteDataSource
) {
    suspend fun fetchWeather(): Flow<Result<*>> {
        return remoteDataSource.fetchWeather()
    }
}