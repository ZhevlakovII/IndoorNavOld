package com.example.indoornavigationar.database.navlocations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "navigation_locations")
data class NavLocations(
    @PrimaryKey
    val locationId: Int,

    @ColumnInfo(name = "loc_name")
    val name: String?,

    @ColumnInfo(name = "loc_name_ru")
    val ruName: String?,

    val coordX1: Double,

    val coordY1: Double,

    val coordX2: Double,

    val coordY2: Double,

    val centerX: Double,

    val centerY: Double
)