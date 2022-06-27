package com.example.indoornavigationar.viewmodels

import androidx.lifecycle.*
import com.example.indoornavigationar.database.navlocations.NavLocations
import com.example.indoornavigationar.database.navlocations.NavLocationsRepo
import com.example.indoornavigationar.database.search.SearchItems
import com.example.indoornavigationar.database.search.SearchItemsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject internal constructor(
    searchItemsRepo: SearchItemsRepo,
    private val navLocationsRepo: NavLocationsRepo
) : ViewModel() {
    lateinit var searchedLocations: LiveData<List<NavLocations>>

    val searchItems: LiveData<List<SearchItems>> =
        searchItemsRepo.getSearchedItems().asLiveData()

    fun getSearchedLocations(name: String) {
        val editString = "%$name%"
        searchedLocations =
            navLocationsRepo.getEnSearchedLocation(editString).asLiveData()
    }
}