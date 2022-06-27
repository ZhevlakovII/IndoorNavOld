package com.example.indoornavigationar.database.navlocations

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavLocationsRepo @Inject constructor(private val navLocationsDao: NavLocationsDao) {
    fun getEnSearchedLocation(locationName: String?) = navLocationsDao.getEnSearchedLocation(locationName)

    fun getItemById(id: Int) = navLocationsDao.getItemById(id)

    fun getAllItems() = navLocationsDao.getAllItems()

}