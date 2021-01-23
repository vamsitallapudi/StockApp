package com.coderefer.stockapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coderefer.stockapp.data.StockRepo
import java.lang.IllegalArgumentException

class ViewModelFactory(private val stockRepo: StockRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(stockRepo) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}