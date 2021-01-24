package com.coderefer.stockapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coderefer.stockapp.R
import com.coderefer.stockapp.data.database.entity.StockResult
import com.coderefer.stockapp.databinding.StockListItemBinding
import com.coderefer.stockapp.util.AppUtils

class StocksRecyclerAdapter(
    private val listener: HomeFragment.RecyclerAddClickListener,
    private val itemClickListener: HomeFragment.RecyclerItemClickListener
) :
    ListAdapter<StockResult, StocksRecyclerAdapter.StockViewHolder>(STOCKS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val postItem = getItem(position)
        holder.bind(postItem, listener, itemClickListener, position)
    }


    class StockViewHolder(binding: StockListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var stockResult: StockResult? = null
        private var mBinding: StockListItemBinding = binding
        fun bind(
            postsWithMultiMedia: StockResult,
            listener: HomeFragment.RecyclerAddClickListener,
            itemClickListener: HomeFragment.RecyclerItemClickListener,
            position: Int
        ) {
            this.stockResult = postsWithMultiMedia
            mBinding.tvStockSymbol.text = this.stockResult?.symbol
            mBinding.tvIndexType.text = this.stockResult?.symbol
            val priceModel = AppUtils.calculatePriceDiff(
                this.stockResult!!.regularMarketPrice,
                this.stockResult!!.regularMarketPreviousClose
            )
            mBinding.tvPrice.apply {
                text = stockResult?.regularMarketPrice.toString()
                setTextColor(
                    if (priceModel.isBull)
                        ContextCompat.getColor(
                            context, R.color.stockIndicatorGreen
                        ) else ContextCompat.getColor(context, R.color.stockIndicatorRed)
                )
            }
            mBinding.tvDifference.text = priceModel.priceDiff
            mBinding.ivAdd.visibility = if (this.stockResult!!.isInDB) View.GONE else View.VISIBLE
            mBinding.ivAdd.setOnClickListener {
                it.visibility = View.GONE
                listener.onAddClicked(position)
            }
            mBinding.root.setOnClickListener {
                itemClickListener.onItemClicked(position)
            }
        }

        companion object {
            fun create(parent: ViewGroup): StockViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding: StockListItemBinding =
                    DataBindingUtil.inflate(
                        inflater,
                        R.layout.stock_list_item,
                        parent,
                        false
                    )
                return StockViewHolder(binding)
            }
        }

    }

    companion object {
        private val STOCKS_COMPARATOR = object : DiffUtil.ItemCallback<StockResult>() {
            override fun areItemsTheSame(oldItem: StockResult, newItem: StockResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StockResult, newItem: StockResult): Boolean {
                return oldItem.symbol == newItem.symbol
            }
        }
    }


}