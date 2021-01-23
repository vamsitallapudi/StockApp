package com.coderefer.stockapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coderefer.stockapp.data.database.dao.StockDao
import com.coderefer.stockapp.data.database.entity.StockResult

@Database(entities = [StockResult::class], version = 1, exportSchema = false)
abstract class StocksRoomDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao

    companion object {

        private const val DATABASE_NAME = "stocks_database"

        @Volatile
        private var instance: StocksRoomDatabase? = null

        fun getDatabase(context: Context
        ): StocksRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): StocksRoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                StocksRoomDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}