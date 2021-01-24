package com.coderefer.stockapp.data.database.dao

import androidx.room.*
import com.coderefer.stockapp.data.database.entity.StockResult

@Dao
interface StockDao {

    @Transaction
    @Query("SELECT * FROM StockResult ORDER BY symbol asc")
    fun getStockResults(): List<StockResult>

    @Query("SELECT * FROM StockResult where symbol = :symbol")
    fun checkStock(symbol:String): StockResult?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockResult: StockResult): Long //return type is key here
}