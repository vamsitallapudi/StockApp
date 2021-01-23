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
import com.coderefer.stockapp.R
import com.coderefer.stockapp.data.Result

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = (activity as HomeActivity).obtainViewModel()
        fetchStocks()
        observeStocksLiveData()
        return v
    }

    private fun observeStocksLiveData() {
        viewModel.stockLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Result.Success -> {
                    Toast.makeText(activity, it.data.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun fetchStocks() {
       viewModel.fetchStocks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_charts).setOnClickListener(this)
        view.findViewById<Button>(R.id.btn_search).setOnClickListener(this)
        view.findViewById<Button>(R.id.btn_help).setOnClickListener(this)
        view.findViewById<Button>(R.id.btn_prefs).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        lateinit var action: NavDirections
        when (v!!.id) {
            R.id.btn_charts -> {
                action = HomeFragmentDirections.actionHomeFragmentToChartFragment()
            }
            R.id.btn_help -> {
                action = HomeFragmentDirections.actionHomeFragmentToHelpFragment()
            }
            R.id.btn_search -> {
                action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            }
            R.id.btn_prefs -> {
                action = HomeFragmentDirections.actionHomeFragmentToPrefsFragment()
            }
        }
        v.findNavController().navigate(action)

    }
}