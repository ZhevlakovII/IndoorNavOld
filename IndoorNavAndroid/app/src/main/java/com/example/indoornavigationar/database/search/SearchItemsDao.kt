package com.example.indoornavigationar.database.search

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchItemsDao {
    @Query("SELECT * FROM search_items ORDER BY idSearchedItem DESC")
    fun getSearchedItems(): Flow<List<SearchItems>>

    @Insert
    suspend fun insertItem(searchItems: SearchItems)

    @Delete
    suspend fun deleteItem(searchItems: SearchItems)

    @Query("SELECT * FROM search_items ORDER BY idSearchedItem DESC LIMIT 1")
    fun getLastId(): Int
}