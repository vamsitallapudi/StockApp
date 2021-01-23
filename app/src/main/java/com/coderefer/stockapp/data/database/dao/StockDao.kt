package com.coderefer.stockapp.data.database.dao

import androidx.room.*
import com.coderefer.stockapp.data.database.entity.StockResult
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Transaction
    @Query("SELECT * FROM StockResult ORDER BY symbol asc")
    fun getStockResults(): List<StockResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(post: StockResult): Long //return type is key here
}