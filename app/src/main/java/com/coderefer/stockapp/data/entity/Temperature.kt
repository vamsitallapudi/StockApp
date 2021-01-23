package com.coderefer.stockapp.data.entity

import com.google.gson.annotations.SerializedName

data class Temperature (val temp: String,
                        @SerializedName("temp_min")
                        val tempMin:String,
                        @SerializedName("temp_max")
                        val tempMax:String)
