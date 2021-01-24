package com.coderefer.stockapp.data.database.entity.charts

import com.google.gson.annotations.SerializedName

data class Chart(
    @SerializedName("result")
    val result: List<Result>)