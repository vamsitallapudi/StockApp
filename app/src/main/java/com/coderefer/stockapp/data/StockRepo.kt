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


    fun addSources(sources: List<String>) {
        sources.forEach { localDataSource.addSource(it) }
        cache.addAll(sources)
    }


    suspend fun getStockSources(): List<String> = withContext(dispatcherProvider.io) {
        return@withContext getStockSourcesSync()
    }


    private fun getStockSourcesSync(): List<String> {
        if (cache.isNotEmpty()) {
            return cache
        }
        // cache is empty
        val sourceKeys = localDataSource.getKeys()
        if (sourceKeys == null) {
            addSources(defaultSources)
            return defaultSources
        }
        val sources = mutableListOf<String>()
        sourceKeys.forEach {
            sources.add(it)
        }
        cache.addAll(sources)
        return cache
    }
}