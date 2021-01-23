package com.coderefer.stockapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coderefer.stockapp.R
import com.coderefer.stockapp.data.entity.StockResult
import com.coderefer.stockapp.databinding.StockListItemBinding

class StocksRecyclerAdapter : ListAdapter<StockResult, StocksRecyclerAdapter.StockViewHolder>(STOCKS_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val postItem = getItem(position)
        holder.bind(postItem)
    }


    class StockViewHolder(binding: StockListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var stockResult: StockResult? = null
        private var mBinding: StockListItemBinding = binding
        fun bind(postsWithMultiMedia: StockResult) {
            this.stockResult = postsWithMultiMedia
            mBinding.textView.text = this.stockResult?.symbol
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