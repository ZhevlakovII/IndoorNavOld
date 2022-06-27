package com.example.indoornavigationar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.indoornavigationar.R
import com.example.indoornavigationar.database.navlocations.NavLocations
import com.example.indoornavigationar.database.search.SearchItems
import com.example.indoornavigationar.database.search.SearchItemsRepo
import com.example.indoornavigationar.databinding.SearchedRvItemBinding
import com.example.indoornavigationar.ui.SearchFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchedAdapter @Inject internal constructor(
    private val searchedItemsRepo: SearchItemsRepo,
    private val isOpenFromMap: Boolean
): ListAdapter<NavLocations, RecyclerView.ViewHolder>(NavLocationsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchedViewHolder(
            SearchedRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            searchedItemsRepo,
            isOpenFromMap
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val searchedItem = getItem(position)
        (holder as SearchedViewHolder).bind(searchedItem)
        holder.setIcon()
    }

    class SearchedViewHolder(
        private val binding: SearchedRvItemBinding,
        private val searchedItemsRepo: SearchItemsRepo,
        private val isOpenFromMap: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.searchedItem?.let { item ->
                    navigateToPointsEnter(item, it, isOpenFromMap)
                    insertItemInDatabase(item)
                }
            }
        }

        private fun navigateToPointsEnter(
            searchedItem: NavLocations,
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

        fun bind(item: NavLocations) {
            binding.apply {
                searchedItem = item
                executePendingBindings()
            }
        }

        private fun insertItemInDatabase(item: NavLocations) {
            CoroutineScope(Dispatchers.IO).launch {
                val id = searchedItemsRepo.getLastId() + 1
                searchedItemsRepo.insertSearchedItem(SearchItems(id, item.name, item.locationId))
            }
        }

        fun setIcon() {
            binding.iconRvSearched.setImageResource(R.drawable.navigate_dot_icon)
        }
    }
}

private class NavLocationsDiffCallback : DiffUtil.ItemCallback<NavLocations>() {

    override fun areItemsTheSame(oldItem: NavLocations, newItem: NavLocations): Boolean {
        return oldItem.locationId == newItem.locationId
    }

    override fun areContentsTheSame(oldItem: NavLocations, newItem: NavLocations): Boolean {
        return oldItem == newItem
    }
}