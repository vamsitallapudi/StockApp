package com.coderefer.stockapp.data

import android.content.SharedPreferences
import com.coderefer.stockapp.data.database.dao.StockDao

class StockLocalDataSource(val stockDao: StockDao, private val prefs:SharedPreferences) : StockDataSource {

    private fun getMutableKeys(default: Set<String>? = null): MutableSet<String>? {
        return prefs.getStringSet(STOCK_PREFS_SET, default)
    }

    companion object {
        const val STOCK_PREFS_SET = "STOCK_PREFS_SET"
    }
}