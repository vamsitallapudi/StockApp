package com.coderefer.stockapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderefer.stockapp.data.database.entity.StockResult
import com.coderefer.stockapp.databinding.FragmentHomeBinding
import com.coderefer.stockapp.ui.charts.ChartsFragmentArgs
import com.coderefer.stockapp.util.SEARCH_DELAY_TIMER
import java.util.*


class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var stockResultsList: List<StockResult>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as HomeActivity).obtainViewModel()
        }
        val adapter = initRecyclerAdapter()
        getStockSources()
        observeStockSources()
        observeStocksLiveData(adapter)
        implementSearch()
        observeUIState()
        return mBinding.root
    }

    private fun implementSearch() {

        mBinding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                private var timer: Timer = Timer()
                private val DELAY: Long = SEARCH_DELAY_TIMER // milliseconds
                override fun afterTextChanged(s: Editable) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                if (s.toString().isNotEmpty())
                                    fetchStocks(s.toString())
                                else
                                    getStockSources()
                            }
                        },
                        DELAY
                    )
                }
            }
        )
    }


    private fun initRecyclerAdapter(): StocksRecyclerAdapter {
        val addClickListener = object : RecyclerAddClickListener {
            override fun onAddClicked(position: Int) {
                mBinding.viewmodel!!.insertStockToDB(stockResultsList[position])
            }
        }
        val itemClickListener = object : RecyclerItemClickListener {
            override fun onItemClicked(position: Int) {

                val action = HomeFragmentDirections.actionHomeFragmentToChartFragment(stockResultsList[position].symbol)
                findNavController().navigate(action)
            }
        }
        val adapter = StocksRecyclerAdapter(addClickListener, itemClickListener)
        mBinding.recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(activity)
        }
        return adapter
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


    private fun observeStockSources() {
        mBinding.viewmodel!!.stockSourcesLiveData.observe(viewLifecycleOwner, {
            fetchStocks(it)
        })
    }

    private fun observeStocksLiveData(adapter: StocksRecyclerAdapter) {
        mBinding.viewmodel!!.stockLiveData.observe(viewLifecycleOwner, {
            stockResultsList = it
            populateAdapter(adapter, it)
        })
    }

    private fun populateAdapter(adapter: StocksRecyclerAdapter, list: List<StockResult>) {
        adapter.submitList(list)
    }

    private fun fetchStocks(stockName: String) {
        mBinding.viewmodel!!.fetchStocks(stockName)
    }
    private fun getStockSources() {
        mBinding.viewmodel!!.getStockSources()
    }

    override fun onClick(v: View?) {
//        lateinit var action: NavDirections
//        when (v!!.id) {
//            R.id.btn_charts -> {
//                action = HomeFragmentDirections.actionHomeFragmentToChartFragment()
//            }
//            R.id.btn_help -> {
//                action = HomeFragmentDirections.actionHomeFragmentToHelpFragment()
//            }
//            R.id.btn_search -> {
//                action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
//            }
//            R.id.btn_prefs -> {
//                action = HomeFragmentDirections.actionHomeFragmentToPrefsFragment()
//            }
//        }
//        v.findNavController().navigate(action)

    }
    interface RecyclerAddClickListener {
        fun onAddClicked(position:Int)
    }
    interface RecyclerItemClickListener {
        fun onItemClicked(position:Int)
    }
}