package com.coderefer.stockapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "StockResult")
data class StockResult(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "postId")
    @SerializedName("postId")
    val postId: Long,

    @ColumnInfo(name="regularMarketPrice")
    @SerializedName("regularMarketPrice")
    val regularMarketPrice: Double,

    @ColumnInfo(name="regularMarketPreviousClose")
    @SerializedName("regularMarketPreviousClose")
    val regularMarketPreviousClose: Double,

    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    val symbol: String
)