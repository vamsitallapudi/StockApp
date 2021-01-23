package com.coderefer.stockapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderefer.stockapp.data.CoroutineDispatchProvider
import com.coderefer.stockapp.data.Result
import com.coderefer.stockapp.data.StockRepo
import com.coderefer.stockapp.data.entity.Stock
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: use DI to inject repo
class HomeViewModel(private val repo: StockRepo) : ViewModel() {

    private val weatherMutableLiveData = MutableLiveData<Result<Stock>>()
    val stockLiveData: LiveData<Result<Stock>>
        get() = weatherMutableLiveData

    val dispatchProvider by lazy {
        CoroutineDispatchProvider()
    }

    fun fetchWeather(): Job {
        return viewModelScope.launch(dispatchProvider.io) {
            withContext(dispatchProvider.main) {
                showLoading()
            }
            val result = repo.fetchWeather()
            result.collect {
                when (it) {
                    is Result.Success<*> -> {
                        weatherMutableLiveData.postValue(it as Result<Stock>)
                    }
                    is Result.Error -> {
                        weatherMutableLiveData.postValue(it)
                    }
                    is Result.Loading -> {
                        //TODO
                    }
                }
            }
        }
    }

    private fun showLoading() {
//        TODO("Not yet implemented")
    }
}