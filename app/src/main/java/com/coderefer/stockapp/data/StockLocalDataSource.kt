package com.coderefer.stockapp.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.coderefer.stockapp.data.database.dao.StockDao

class StockLocalDataSource(val stockDao: StockDao, private val prefs:SharedPreferences) : StockDataSource {
    /**
     * Get all sources
     */
    fun getKeys(): Set<String>? = getMutableKeys()

    /**
     * Add a source and set the active state for the source
     */
    fun addSource(source: String) {
        val sources = getMutableKeys(mutableSetOf())
        sources?.add(source)
        prefs.edit {
            remove(STOCK_PREFS_SET)
            apply()
            putStringSet(STOCK_PREFS_SET, sources)
        }
    }

    /**
     * Update the active state of a source
     */
    fun updateSource(source: String, isActive: Boolean) {
        prefs.edit { putBoolean(source, isActive) }
    }

    /**
     * Remove source and the corresponding active state.
     * @return true if the source was removed, false otherwise
     */
    fun removeSource(source: String): Boolean {
        var removed = false
        val sources = getMutableKeys(mutableSetOf())
        sources?.remove(source)
        prefs.edit {
            putStringSet(STOCK_PREFS_SET, sources)
            remove(source)
            removed = true
        }
        return removed
    }

    fun getSourceActiveState(source: String): Boolean {
        return prefs.getBoolean(source, false)
    }

    private fun getMutableKeys(default: Set<String>? = null): MutableSet<String>? {
        return prefs.getStringSet(STOCK_PREFS_SET, default)
    }

    companion object {
        const val STOCK_PREFS_SET = "STOCK_PREFS_SET"
    }
}