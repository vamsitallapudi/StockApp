package com.coderefer.stockapp.util

import com.coderefer.stockapp.data.entity.PriceModel
import java.lang.StringBuilder
import kotlin.math.abs

class AppUtils {
    companion object {
        @JvmStatic
        fun calculatePriceDiff(currentPrice: Double, oldPrice: Double): PriceModel {
            val currentRoundedPrice = currentPrice.roundTo(2)
            val oldRoundedPrice = oldPrice.roundTo(2)
            val sb = StringBuilder()
            val priceDiff = (currentPrice - oldPrice).roundTo(2)
            if (priceDiff > 0) sb.append("+")
            sb.append(priceDiff)
            sb.append(" (")
            val percentDiff = calculatePercentage(currentRoundedPrice, oldRoundedPrice)
            if (priceDiff > 0) sb.append("+")
            sb.append(percentDiff)
            sb.append("%)")
            return PriceModel(sb.toString(), currentPrice > oldPrice)
        }

        private fun calculatePercentage(currentPrice: Double, oldPrice: Double): Double {
            return (((currentPrice - oldPrice) / oldPrice) * 100).roundTo(2)
        }
    }
}