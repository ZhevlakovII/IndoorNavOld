package com.example.indoornavigationar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.indoornavigationar.R
import com.example.indoornavigationar.database.search.SearchItems
import com.example.indoornavigationar.database.search.SearchItemsRepo
import com.example.indoornavigationar.databinding.HistoryRvItemBinding
import com.example.indoornavigationar.ui.SearchFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryAdapter @Inject internal constructor(
    private val searchedItemsRepo: SearchItemsRepo,
    private val isOpenFromMap: Boolean
): ListAdapter<SearchItems, RecyclerView.ViewHolder>(SearchItemsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryViewHolder(
            HistoryRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            searchedItemsRepo,
            isOpenFromMap
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val history = getItem(position)
        (holder as HistoryViewHolder).bind(history)
        holder.setIcon()
    }

    class HistoryViewHolder(
        private val binding: HistoryRvItemBinding,
        private val searchedItemsRepo: SearchItemsRepo,
        private val isOpenFromMap: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.history?.let { item ->
                    navigateToPointsEnter(item, it, isOpenFromMap)
                    reInsertSearchedItem(item)

                }
            }
        }

        private fun navigateToPointsEnter(
            searchedItem: SearchItems,
            view: View,
            isOpenFromMap: Boolean
        ) {
            if (isOpenFromMap) {
                val action =
                    SearchFragmentDirections.actionSearchNavigationToPointsEnter(isOpenFromMap, searchedItem.locationId)
                view.findNavController().navigate(action)
            } else {
                view.findNavController().previousBackStackEntry?.savedStateHandle?.set("locId", searchedItem.locationId)
                view.findNavController().navigateUp()
            }
        }

        fun bind(item: SearchItems) {
            binding.apply {
                history = item
                executePendingBindings()
            }
        }

        private fun reInsertSearchedItem(item: SearchItems) {
            CoroutineScope(Dispatchers.IO).launch {
                val id = searchedItemsRepo.getLastId() + 1
                searchedItemsRepo.deleteSearchedItem(item)
                searchedItemsRepo.insertSearchedItem(SearchItems(id, item.name, item.locationId))
            }
        }

        fun setIcon() {
            binding.iconRvHistory.setImageResource(R.drawable.history_icon)
        }
    }
}

private class SearchItemsDiffCallback : DiffUtil.ItemCallback<SearchItems>() {

    override fun areItemsTheSame(oldItem: SearchItems, newItem: SearchItems): Boolean {
        return oldItem.idSearchedItem == newItem.idSearchedItem
    }

    override fun areContentsTheSame(oldItem: SearchItems, newItem: SearchItems): Boolean {
        return oldItem == newItem
    }
}