package com.example.indoornavigationar.database.navlocations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NavLocationsDao {
    @Query("SELECT * FROM navigation_locations WHERE loc_name LIKE :locationName")
    fun getEnSearchedLocation(locationName: String?): Flow<List<NavLocations>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<NavLocations>)

    @Query("SELECT * FROM navigation_locations WHERE locationId LIKE :id")
    fun getItemById(id: Int): Flow<NavLocations>

    @Query("SELECT * FROM navigation_locations")
    fun getAllItems(): Flow<List<NavLocations>>

    @Query("SELECT centerX FROM navigation_locations WHERE locationId LIKE :id")
    fun getX(id: Int): Double

    @Query("SELECT centerY FROM navigation_locations WHERE locationId LIKE :id")
    fun getY(id: Int): Double
}