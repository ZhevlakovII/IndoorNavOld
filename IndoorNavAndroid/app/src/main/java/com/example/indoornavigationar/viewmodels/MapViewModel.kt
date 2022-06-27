package com.example.indoornavigationar.viewmodels

import android.app.Application
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.example.indoornavigationar.R
import com.example.indoornavigationar.database.navlocations.NavLocationsRepo
import com.example.indoornavigationar.utilites.CheckMatchingCoordinates
import com.example.indoornavigationar.utilites.MapPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject internal constructor(
    private val application: Application,
    navLocationsRepo: NavLocationsRepo,
    private val checkMatchingCoordinates: CheckMatchingCoordinates
) : ViewModel() {
    private var markerCount: Int = 0
    private var listOfMarkerId: MutableList<Int> = mutableListOf()

    var cardActiveState = MutableLiveData<Boolean>().apply {
        value = false
    }

    val locations = navLocationsRepo.getAllItems().asLiveData()
    private var locationName: String? = null
    private var isLocationObservable: Boolean = false
    private lateinit var mapPoint: MapPoint
    private var findResult: Int = -1



    private val tileStreamProvider =
        TileStreamProvider { row, col, zoomLvl ->
            try {
                application.applicationContext.assets.open("homePlan/$zoomLvl/$row/$col.png")
            } catch (e: Exception) {
                null
            }
        }

    val state: MapState by mutableStateOf(
        MapState(3, 1024, 1024).apply {
            addLayer(tileStreamProvider)
            onTap { x, y ->
                if (markerCount == 0) {
                    addMarkerOnMap(x, y)
                } else {
                    removeMarkerOnMap()
                }
            }
            enableRotation()
        }
    )

    private fun addMarkerOnMap(x: Double, y: Double) {
        val result = checkMatchingCoordinates.validateStartCoordinates(x, y)

        if (result) {
            listOfMarkerId.add(markerCount + 1)
            state.addMarker("${listOfMarkerId[listOfMarkerId.lastIndex]}", x, y) {
                Icon(
                    painter = painterResource(id = R.drawable.navigate_dot_icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color(R.color.interface_primary)
                )
            }
            markerCount++

            if (isLocationObservable) {
                findPointLocation(x, y)
                locationName?.let { setCoordinates(x, y, it) }
                changeCardState(true)
            }
        }
    }

    private fun removeMarkerOnMap() {
        state.removeMarker(listOfMarkerId[listOfMarkerId.lastIndex].toString())
        listOfMarkerId.removeLast()
        changeCardState(false)

        markerCount--
    }

    private fun changeCardState(state: Boolean) {
        cardActiveState.value = state
    }

    private fun findPointLocation(x: Double, y: Double) {

        for(location in locations.value!!) {
            if (x >= location.coordX1 && x <= location.coordX2)
                if (y >= location.coordY1 && y <= location.coordY2) {
                    findResult = location.locationId
                    locationName = location.name
                }
        }
    }

    fun setLocationObservable() {
        isLocationObservable = true
    }

    private fun setCoordinates(x: Double, y: Double, name: String) {
        mapPoint = MapPoint(x, y, name)
    }

    fun getLocationName(): String {
        return locationName.toString()
    }

    fun getCoordinates(): MapPoint {
        return mapPoint
    }


    fun getLocId(): Int {
        return findResult
    }
}