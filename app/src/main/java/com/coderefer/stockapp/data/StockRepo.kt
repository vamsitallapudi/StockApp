package com.coderefer.stockapp.data

import kotlinx.coroutines.flow.Flow

class StockRepo(
    private val localDataSource: StockLocalDataSource,
    private val remoteDataSource: StockRemoteDataSource
) {
    suspend fun fetchStocks(stockName: String?): Flow<Result<*>> {
        return remoteDataSource.fetchStocks(stockName)
    }
}