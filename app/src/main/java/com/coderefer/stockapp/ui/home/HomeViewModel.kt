package com.coderefer.stockapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderefer.stockapp.data.CoroutineDispatchProvider
import com.coderefer.stockapp.data.Result
import com.coderefer.stockapp.data.StockRepo
import com.coderefer.stockapp.data.database.entity.Stock
import com.coderefer.stockapp.data.database.entity.StockResult
import com.coderefer.stockapp.data.database.entity.charts.Chart
import com.coderefer.stockapp.data.database.entity.charts.ChartResp
import com.coderefer.stockapp.util.DEFAULT_STOCKS
import com.coderefer.stockapp.util.event.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: use DI to inject repo
class HomeViewModel(private val repo: StockRepo) : ViewModel() {

    private val stockMutableLiveData = MutableLiveData<List<StockResult>>()
    val stockLiveData: LiveData<List<StockResult>>
        get() = stockMutableLiveData

    private val stockInsertMutableLiveData = MutableLiveData<Boolean>()
    val stockInsertLiveData: LiveData<Boolean>
        get() = stockInsertMutableLiveData

    private val stockSourcesMutableLiveData = MutableLiveData<String>()
    val stockSourcesLiveData: LiveData<String>
        get() = stockSourcesMutableLiveData

    private val chartMutableLiveData = MutableLiveData<ChartResp>()
    val chartLiveData: LiveData<ChartResp>
        get() = chartMutableLiveData




    private val _uiState = MutableLiveData<HomeUIModel>()
    val uiState: LiveData<HomeUIModel>
        get() = _uiState

    val dispatchProvider by lazy {
        CoroutineDispatchProvider()
    }

    fun getStockSources(): Job {
        return viewModelScope.launch(dispatchProvider.io) {
            stockSourcesMutableLiveData.postValue(repo.getStockSources())
        }
    }

    fun insertStockToDB(stockResult: StockResult) : Job{
        return viewModelScope.launch(dispatchProvider.io) {
            stockInsertMutableLiveData.postValue(repo.insertStockInDb(stockResult))
        }
    }

    fun fetchStocks(stockName: String? = null): Job {
        return viewModelScope.launch(dispatchProvider.io) {
            withContext(dispatchProvider.main) {
                showLoading()
            }
            var stocks = stockName
            if (stockName == null)
                stocks = DEFAULT_STOCKS
            val result = repo.fetchStocks(stocks)
            result.collect {
                hideLoading()
                when (it) {
                    is Result.Success<*> -> {
                        val stockResultsList = (it.data as Stock).stockQuote.stockResults
                        stockResultsList.forEach { stockResult ->
                            stockResult.isInDB = repo.isStockInDb(stockResult.symbol)
                        }
                        stockMutableLiveData.postValue(stockResultsList)
                    }
                    is Result.Error -> {
//                        showErrorScreen()
                    }
                    is Result.Loading -> {
                        //DO NOTHING
                    }
                }
            }
        }
    }

    fun fetchCharts(stock:String) : Job {
        return viewModelScope.launch(dispatchProvider.io) {
            withContext(dispatchProvider.main) {
                showLoading()
            }

            val result = repo.fetchCharts(stock)
            result.collect {
                hideLoading()
                when (it) {
                    is Result.Success<*> -> {
                        chartMutableLiveData.postValue(it.data as ChartResp)
                    }
                    is Result.Error -> {
//                        showErrorScreen()
                    }
                    is Result.Loading -> {
                        //DO NOTHING
                    }
                }
            }
        }
    }

    private fun showLoading() {
        emitUIState(showProgress = true)
    }

    private fun hideLoading() {
        emitUIState(showProgress = false)
    }


    private fun emitUIState(
        showProgress: Boolean = false,
        showError: Event<Int>? = null,
        showSuccess: Event<HomeUIModel>? = null
    ) {
        val uiModel = HomeUIModel(showProgress, showError, showSuccess)
        _uiState.postValue(uiModel)
    }

    /**
     * UI model for [HomeActivity]
     */
    data class HomeUIModel(
        val showProgress: Boolean,
        val showError: Event<Int>?,
        val showSuccess: Event<HomeUIModel>?
    )
}