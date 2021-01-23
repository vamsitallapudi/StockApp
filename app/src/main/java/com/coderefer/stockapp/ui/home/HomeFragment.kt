package com.coderefer.stockapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderefer.stockapp.R
import com.coderefer.stockapp.data.Result
import com.coderefer.stockapp.data.entity.Stock
import com.coderefer.stockapp.data.entity.StockResult
import com.coderefer.stockapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var mBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as HomeActivity).obtainViewModel()
        }
        val adapter = initRecyclerAdapter()
        fetchStocks()
        observeStocksLiveData(adapter)
        return mBinding.root
    }


    private fun initRecyclerAdapter() : StocksRecyclerAdapter {
        val adapter = StocksRecyclerAdapter()
        mBinding.recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(activity)
        }
        return adapter
    }

    private fun observeStocksLiveData(adapter: StocksRecyclerAdapter) {
        mBinding.viewmodel!!.stockLiveData.observe(viewLifecycleOwner, {
            when(it) {
                is Result.Success -> {
                    Toast.makeText(activity, it.data.toString(), Toast.LENGTH_LONG).show()
                    val stockList = (it.data as Stock).stockQuote.stockResults
                    populateAdapter(adapter, stockList)
                }
                else -> {
                    //TODO
                }
            }
        })
    }

    private fun populateAdapter(adapter: StocksRecyclerAdapter,list:List<StockResult>) {
        adapter.submitList(list)
    }

    private fun fetchStocks() {
       mBinding.viewmodel!!.fetchStocks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<Button>(R.id.btn_charts).setOnClickListener(this)
//        view.findViewById<Button>(R.id.btn_search).setOnClickListener(this)
//        view.findViewById<Button>(R.id.btn_help).setOnClickListener(this)
//        view.findViewById<Button>(R.id.btn_prefs).setOnClickListener(this)
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
}