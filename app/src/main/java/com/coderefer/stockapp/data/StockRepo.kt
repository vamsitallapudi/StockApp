package com.coderefer.stockapp.data

import com.coderefer.stockapp.data.database.entity.StockResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class StockRepo(
    private val defaultSources: List<String>,
    private val localDataSource: StockLocalDataSource,
    private val remoteDataSource: StockRemoteDataSource,
    private val dispatcherProvider: CoroutineDispatchProvider
) {

    suspend fun fetchStocks(stockName: String?): Flow<Result<*>> {
        return remoteDataSource.fetchStocks(stockName)
    }
    suspend fun fetchCharts(stockName: String?): Flow<Result<*>> {
        return remoteDataSource.fetchCharts(stockName)
    }

    suspend fun getStockSources(): String = withContext(dispatcherProvider.io) {
        val result = localDataSource.stockDao.getStockResults()
        val sb = StringBuilder()
        if (result.isNullOrEmpty()) {
            defaultSources.forEach {
                sb.append(it)
                sb.append(",")
            }
        } else {
            result.forEach {
                sb.append(it.symbol)
                sb.append(",")
            }
        }
        sb.toString()
    }

    fun isStockInDb(symbol: String): Boolean {
        return localDataSource.stockDao.checkStock(symbol) != null
    }

    suspend fun insertStockInDb(stockResult: StockResult): Boolean? {
        return localDataSource.stockDao.insertStock(stockResult) != null
    }

}