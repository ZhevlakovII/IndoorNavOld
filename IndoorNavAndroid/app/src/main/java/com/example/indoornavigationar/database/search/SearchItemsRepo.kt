package com.example.indoornavigationar.database.search

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchItemsRepo @Inject constructor(private val searchItemsDao: SearchItemsDao) {

    fun getSearchedItems() = searchItemsDao.getSearchedItems()

    suspend fun insertSearchedItem(item: SearchItems) = searchItemsDao.insertItem(item)

    suspend fun deleteSearchedItem(item: SearchItems) = searchItemsDao.deleteItem(item)

    fun getLastId() = searchItemsDao.getLastId()
}