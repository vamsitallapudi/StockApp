package com.coderefer.stockapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.coderefer.stockapp.R
import com.coderefer.stockapp.StockApp

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        TODO: remove this after DI is added
        viewModelFactory = ViewModelFactory((application as StockApp).stockRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    fun obtainViewModel(): HomeViewModel {
        return viewModel
    }
}