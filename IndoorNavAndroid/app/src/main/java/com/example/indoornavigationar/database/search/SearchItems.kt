package com.example.indoornavigationar.database.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "search_items")
data class SearchItems(
    @PrimaryKey
    val idSearchedItem: Int,

    @ColumnInfo(name = "itemName")
    val name: String?,

    @ColumnInfo(name = "locationId")
    val locationId: Int
)