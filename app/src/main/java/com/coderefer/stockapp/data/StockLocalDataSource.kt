package com.coderefer.stockapp.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.coderefer.stockapp.data.database.dao.StockDao
import kotlinx.coroutines.flow.Flow

class StockLocalDataSource(val stockDao: StockDao, private val prefs:SharedPreferences) : StockDataSource {

    private fun getMutableKeys(default: Set<String>? = null): MutableSet<String>? {
        return prefs.getStringSet(STOCK_PREFS_SET, default)
    }

    companion object {
        const val STOCK_PREFS_SET = "STOCK_PREFS_SET"
    }
}