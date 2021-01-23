package com.coderefer.stockapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class StockRepo(
    private val defaultSources: List<String>,
    private val localDataSource: StockLocalDataSource,
    private val remoteDataSource: StockRemoteDataSource,
    private val dispatcherProvider: CoroutineDispatchProvider
) {

    private val cache = mutableListOf<String>()

    suspend fun fetchStocks(stockName: String?): Flow<Result<*>> {
        return remoteDataSource.fetchStocks(stockName)
    }

    fun getStockSourcesFromLocal() {
        localDataSource.stockDao.getStockResults()
    }


    fun addSources(sources: List<String>) {
        sources.forEach { localDataSource.addSource(it) }
        cache.addAll(sources)
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

}