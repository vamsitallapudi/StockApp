package com.coderefer.stockapp.ui.charts

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.coderefer.stockapp.R
import com.coderefer.stockapp.databinding.FragmentChartsBinding
import com.coderefer.stockapp.ui.home.HomeActivity
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import java.lang.Exception


class ChartsFragment : Fragment() {

    private lateinit var mBinding: FragmentChartsBinding
    val args: ChartsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mBinding = FragmentChartsBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as HomeActivity).obtainViewModel()
        }
        initializeCandleStickChart()
        implementListenersForRange()
        setTitle()
        fetchChartData()
        observeChartData()
        observeUIState()
        return mBinding.root
    }

    private fun setTitle() {
        mBinding.tvStockSymbol.text = args.stockSymbol
    }

    private fun implementListenersForRange() {
        mBinding.tv1D.setOnClickListener { fetchChartData(range = "1D") }
        mBinding.tv3M.setOnClickListener { fetchChartData(range = "3M") }
        mBinding.tv6M.setOnClickListener { fetchChartData(range = "6M") }
    }

    private fun observeChartData() {
        mBinding.viewmodel!!.chartLiveData.observe(viewLifecycleOwner, {

            val candleStickValues = mutableListOf<CandleEntry>()
            try {
                val quotes = it.chart.result[0].indicators.quote
                val close = quotes[0].close
                val open = quotes[0].open
                val low = quotes[0].low
                val high = quotes[0].high

                for (i in close.indices) {
                    candleStickValues.add(
                        CandleEntry(
                            i.toFloat(),
                            high[i].toFloat(),
                            low[i].toFloat(),
                            open[i].toFloat(),
                            close[i].toFloat()
                        )
                    )
                }
                createCandleDataSet(candleStickValues)
            } catch (e: Exception) {
                Log.e(ChartsFragment::class.simpleName, e.toString())
            }
        })
    }

    private fun createCandleDataSet(candleStickValues: List<CandleEntry>) {
        val set1 = CandleDataSet(candleStickValues, "DataSet 1")
        set1.color = Color.rgb(80, 80, 80)
        set1.shadowColor = ContextCompat.getColor(activity as Context, R.color.stockIndicatorGrey)
        set1.shadowWidth = 0.8f
        set1.decreasingColor = ContextCompat.getColor(
            activity as Context, R.color.stockIndicatorGreen
        )
        set1.decreasingPaintStyle = Paint.Style.FILL
        set1.increasingColor = ContextCompat.getColor(
            activity as Context, R.color.stockIndicatorRed
        )
        set1.increasingPaintStyle = Paint.Style.FILL
        set1.neutralColor = Color.LTGRAY
        set1.setDrawValues(false)
        val data = CandleData(set1)
        mBinding.candleStickChart.data = data
        mBinding.candleStickChart.invalidate()
    }

    private fun initializeCandleStickChart() {
        mBinding.candleStickChart.isHighlightPerDragEnabled = true

        mBinding.candleStickChart.setDrawBorders(true)

        mBinding.candleStickChart.setBorderColor(
            ContextCompat.getColor(
                activity as Context,
                R.color.stockIndicatorDarkGrey
            )
        )

        val yAxis = mBinding.candleStickChart.axisLeft
        val rightAxis = mBinding.candleStickChart.axisRight
        yAxis.setDrawGridLines(false)
        rightAxis.setDrawGridLines(false)
        mBinding.candleStickChart.requestDisallowInterceptTouchEvent(true)

        val xAxis = mBinding.candleStickChart.xAxis

        xAxis.setDrawGridLines(false) // disable x axis grid lines

        xAxis.setDrawLabels(false)
        rightAxis.textColor = Color.WHITE
        yAxis.setDrawLabels(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setAvoidFirstLastClipping(true)

        val l = mBinding.candleStickChart.legend
        l.isEnabled = false
    }


    private fun observeUIState() {
        mBinding.viewmodel!!.uiState.observe(viewLifecycleOwner, Observer {
            val uiModel = it ?: return@Observer
            showProgressIndicator(uiModel.showProgress)
        })
    }

    private fun showProgressIndicator(showProgress: Boolean) {
        mBinding.progressBar.visibility = if (showProgress) View.VISIBLE else View.GONE
    }

    private fun fetchChartData(range:String? =null) {
        mBinding.viewmodel!!.fetchCharts(args.stockSymbol,range)
    }
}